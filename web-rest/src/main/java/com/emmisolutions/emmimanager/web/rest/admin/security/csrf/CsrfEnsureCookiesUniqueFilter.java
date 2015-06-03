package com.emmisolutions.emmimanager.web.rest.admin.security.csrf;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Wraps the response so we can ensure that setCookie calls only send one cookie back per
 * name.
 * <p/>
 * Register this before the CsrfFilter so that the outbound works properly.
 */
public class CsrfEnsureCookiesUniqueFilter extends OncePerRequestFilter {

    private final DoubleSubmitSignedCsrfTokenRepository repository;

    /**
     * Create a unique cookie filter for the DoubleSubmitSignedCsrfTokenRepository
     *
     * @param repository containing SecurityTokenCookieParameterNameTuple with cookie names
     */
    public CsrfEnsureCookiesUniqueFilter(DoubleSubmitSignedCsrfTokenRepository repository) {
        Assert.notNull(repository);
        this.repository = repository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(request, new UniqueCookieResponseWrapper(response));
    }

    class UniqueCookieResponseWrapper extends HttpServletResponseWrapper {

        public UniqueCookieResponseWrapper(HttpServletResponse response) {
            super(response);
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
        }
    }
}
