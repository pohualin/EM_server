package com.emmisolutions.emmimanager.web.rest.admin.security.csrf;

import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is the CSRF hash validation checking filter. This filter adds XSS protection
 * to the double submit pattern for every request except any request
 * where there is not already a security token. (e.g. the login request)
 * <p/>
 * It does two things:
 * <p/>
 * 1. Wraps the request with a wrapper that can indicate CSRF validity
 * <p/>
 * 2. Wraps the response so we can ensure that setCookie calls only send one cookie back per
 * name.
 * <p/>
 * Register this BEFORE the CsrfFilter to enable CSRF hash validation occurs.
 */
public class CsrfTokenValidationFilter extends OncePerRequestFilter {

    private final Logger LOGGER = LoggerFactory.getLogger(CsrfTokenValidationFilter.class);

    private final DoubleSubmitSignedCsrfTokenRepository repository;

    /**
     * Create a unique cookie filter for the DoubleSubmitSignedCsrfTokenRepository
     *
     * @param repository containing SecurityTokenCookieParameterNameTuple with cookie names
     */
    public CsrfTokenValidationFilter(DoubleSubmitSignedCsrfTokenRepository repository) {
        Assert.notNull(repository);
        this.repository = repository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        CsrfValidRequestWrapper updatedCookieRequestWrapper =
                new CsrfValidRequestWrapper(request, determineHashStatus(request));
        filterChain.doFilter(updatedCookieRequestWrapper,
                new UniqueCookieResponseWrapper(updatedCookieRequestWrapper, response));
    }

    private enum HashStatus {
        VALID, INVALID, CSRF_NOT_PRESENT, NOT_LOGGED_IN
    }

    /**
     * This method determines if the csrf token embedded in the request has a valid
     * hash signature. The hash is based upon the authentication token which also
     * comes in the request (as a cookie).
     *
     * @param request to find the CSRF token and authentication token
     * @return NOT_LOGGED_IN: when there is no authentication token or no CSRF token
     * VALID: when the hash of the passed CSRF token matches a newly generated hash from the authentication token
     * INVALID: when the hash of the passed CSRF token does not match a newly generated hash from the authentication token
     */
    private HashStatus determineHashStatus(HttpServletRequest request) {
        HashStatus status = HashStatus.CSRF_NOT_PRESENT;
        CsrfToken csrfTokenInRequest = repository.loadToken(request);
        if (csrfTokenInRequest != null) {
            LOGGER.debug("CSRF Token present in request: {}:{}",
                    csrfTokenInRequest.getHeaderName(), csrfTokenInRequest.getToken());
            status = HashStatus.NOT_LOGGED_IN;
            SECURITY_TOKEN_LOOP:
            for (DoubleSubmitSignedCsrfTokenRepository.SecurityTokenCookieParameterNameTuple
                    cookieParameterNamePair : repository.getCookieParameterNamePairs()) {
                // find the security token that is paired with the CSRF token
                if (request.getCookies() != null) {
                    for (Cookie cookie : request.getCookies()) {
                        if (StringUtils.equals(cookie.getName(), cookieParameterNamePair.getSecurityTokenName())) {
                            LOGGER.debug("Security Token present in request: {}:{}", cookie.getName(), cookie.getValue());
                            // regenerate a token based upon the current request
                            CsrfToken validationToken = repository.generateToken(request);

                            // compare the newly generated token hash with the passed token hash
                            String generatedHash = validationToken.getToken().substring(validationToken.getToken().lastIndexOf('-'));
                            String passedHash = csrfTokenInRequest.getToken().substring(csrfTokenInRequest.getToken().lastIndexOf('-'));

                            /*
                                if the hashes match then the CSRF token has not been tampered with and was generated
                                using the same access token that is presently in the request
                             */
                            status = StringUtils.equals(passedHash, generatedHash) ? HashStatus.VALID : HashStatus.INVALID;
                            break SECURITY_TOKEN_LOOP;
                        }
                    }
                }
            }
        }
        LOGGER.debug("CSRF Token Validity: {}", status);
        return status;
    }

    class CsrfValidRequestWrapper extends HttpServletRequestWrapper {

        private Map<String, Cookie> cookiesWrittenToResponse = new ConcurrentHashMap<>();

        private final HashStatus status;

        /**
         * Constructs a request object wrapping the given request.
         *
         * @param request the original http request
         * @throws IllegalArgumentException if the request is null
         */
        public CsrfValidRequestWrapper(HttpServletRequest request, HashStatus status) {
            super(request);
            this.status = status;
        }

        public Map<String, Cookie> getCookiesWrittenToResponse() {
            return cookiesWrittenToResponse;
        }

        @Override
        public Cookie[] getCookies() {
            Cookie[] realCookies = super.getCookies();
            Cookie[] ret = null;
            if (realCookies != null) {
                // copy the real cookies
                ret = new Cookie[realCookies.length];
                for (int i = 0; i < ret.length; i++) {
                    // use the latest cookie if already overwritten in the response
                    Cookie alreadyUpdated = getCookiesWrittenToResponse().get(realCookies[i].getName());
                    if (alreadyUpdated == null) {
                        alreadyUpdated = realCookies[i];
                    }
                    ret[i] = (Cookie) alreadyUpdated.clone();
                }
            }
            return ret;
        }

        public boolean isValid() {
            return true;
        }
    }

    class UniqueCookieResponseWrapper extends HttpServletResponseWrapper {

        private final CsrfValidRequestWrapper request;

        /**
         * Links the request wrapper with the response wrapper.. it is
         * in this way that we can get cookies set on the response in
         * the request as well.
         *
         * @param request  wrapper that exposes response cookies
         * @param response the original http servlet response
         */
        public UniqueCookieResponseWrapper(CsrfValidRequestWrapper request, HttpServletResponse response) {
            super(response);
            this.request = request;
        }

        boolean alreadyWritten; // written to the response

        private Map<String, Cookie> cookies = new ConcurrentHashMap<>();

        private void writeUniqueCookies() {
            if (!alreadyWritten) {
                alreadyWritten = true;
                for (Cookie cookie : cookies.values()) {
                    super.addCookie(cookie);
                }
            }
        }

        @Override
        public void sendError(int sc) throws IOException {
            writeUniqueCookies();
            super.sendError(sc);
        }

        @Override
        public void sendError(int sc, String msg) throws IOException {
            writeUniqueCookies();
            super.sendError(sc, msg);
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            writeUniqueCookies();
            return super.getOutputStream();
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            writeUniqueCookies();
            return super.getWriter();
        }

        @Override
        public void addCookie(Cookie cookie) {
            boolean alreadyHandled = false;

            // find all possible cookie values for CSRF, ensure they are only added once
            for (DoubleSubmitSignedCsrfTokenRepository.SecurityTokenCookieParameterNameTuple
                    cookieParameterNamePair : repository.getCookieParameterNamePairs()) {
                if (StringUtils.equals(cookieParameterNamePair.getXsrfCookieName(), cookie.getName())) {
                    cookies.put(cookie.getName(), cookie);
                    alreadyHandled = true;
                    break;
                }
            }
            if (!alreadyHandled) {
                // for non-CSRF cookies, do the original
                super.addCookie(cookie);
            }

            // track all cookie writes on the request
            request.getCookiesWrittenToResponse().put(cookie.getName(), cookie);
        }
    }
}
