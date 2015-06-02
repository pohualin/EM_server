package com.emmisolutions.emmimanager.web.rest.admin.security.csrf;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.util.Assert;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * The CSRF token repository implementation. This is essentially
 * a double submit solution. This means that both the request
 * parameter and cookie value must match in order for the request
 * to be valid. It is also a secure implementation in that the security
 * token (used to authenticate the user) is used to 'sign' the generated
 * csrf token.
 * <p/>
 * The security token cookie name, and accompanying xsrfCookie and xsrfHeader
 * names are specified by the constructor. The SecurityTokenCookieParameterNameTuple
 * is processed in the order that they are passed.. so the first match found in
 * the tuple is used for that particular request. This allows us to do things like
 * impersonation where the security token is stored differently than the normal
 */
public class DoubleSubmitSignedCsrfTokenRepository implements CsrfTokenRepository {

    private final List<SecurityTokenCookieParameterNameTuple> cookieParameterNamePairs;

    /**
     * Create a new repository
     *
     * @param securityTokenCookieParameterNameTuples this is the list of tuples to search through
     *                                               to generate csrf tokens. The first security
     *                                               token matching a cookie in the request will
     *                                               be used.
     */
    public DoubleSubmitSignedCsrfTokenRepository(
            SecurityTokenCookieParameterNameTuple... securityTokenCookieParameterNameTuples) {
        Assert.notEmpty(securityTokenCookieParameterNameTuples);
        this.cookieParameterNamePairs = Arrays.asList(securityTokenCookieParameterNameTuples);
    }

    @Override
    public CsrfToken generateToken(HttpServletRequest request) {
        StringBuilder csrfToken = new StringBuilder(UUID.randomUUID().toString());
        StringBuilder toHash = new StringBuilder(String.valueOf(System.nanoTime()));
        String xsrfParameterName = null;
        SECURITY_TOKEN_LOOP:
        for (SecurityTokenCookieParameterNameTuple cookieParameterNamePair : cookieParameterNamePairs) {
            xsrfParameterName = cookieParameterNamePair.xsrfHeaderName;
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if (StringUtils.equalsIgnoreCase(cookie.getName(), cookieParameterNamePair.securityTokenName)) {
                        toHash.append(cookie.getValue());
                        break SECURITY_TOKEN_LOOP;
                    }
                }
            }
        }
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No MD5 algorithm available!");
        }
        return new DefaultCsrfToken(xsrfParameterName, xsrfParameterName,
                csrfToken.append("-").append(
                        new String(Hex.encode(digest.digest(toHash.toString().getBytes()))))
                        .toString());
    }

    @Override
    public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
        for (SecurityTokenCookieParameterNameTuple cookieParameterNamePair : cookieParameterNamePairs) {
            if (token != null) {
                if (StringUtils.equalsIgnoreCase(token.getHeaderName(), cookieParameterNamePair.xsrfHeaderName)) {
                    Cookie csrfCookie = new Cookie(cookieParameterNamePair.xsrfCookieName, token.getToken());
                    csrfCookie.setPath("/");
                    csrfCookie.setMaxAge(-1);
                    csrfCookie.setSecure(isSecureRequest(request));
                    response.addCookie(csrfCookie);
                    break;
                }
            } else {
                // invalidation of csrf should happen if the token is null
                Cookie csrfCookie = new Cookie(cookieParameterNamePair.xsrfCookieName, null);
                csrfCookie.setPath("/");
                csrfCookie.setMaxAge(0);
                csrfCookie.setSecure(isSecureRequest(request));
                response.addCookie(csrfCookie);
            }
        }
    }

    @Override
    public CsrfToken loadToken(HttpServletRequest request) {
        String csrfFromCookie = null;
        String xsrfParameterName = null;
        if (request.getCookies() != null) {
            SECURITY_TOKEN_LOOP:
            for (SecurityTokenCookieParameterNameTuple cookieParameterNamePair : cookieParameterNamePairs) {
                xsrfParameterName = cookieParameterNamePair.xsrfHeaderName;
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equalsIgnoreCase(cookieParameterNamePair.xsrfCookieName)) {
                        csrfFromCookie = cookie.getValue();
                        break SECURITY_TOKEN_LOOP;
                    }
                }
            }
        }
        return csrfFromCookie != null ?
                new DefaultCsrfToken(xsrfParameterName, xsrfParameterName, csrfFromCookie) : null;
    }

    private boolean isSecureRequest(HttpServletRequest request) {
        return request.isSecure() ||
                StringUtils.equalsIgnoreCase(request.getHeader("X-Forwarded-Ssl"), "on");
    }

    /**
     * Combines the security token cookie name (used to authenticate the user) with the
     * xsrfCookie name and xsrf header/parameter name.
     */
    public static class SecurityTokenCookieParameterNameTuple {
        private final String securityTokenName;
        private final String xsrfCookieName;
        private final String xsrfHeaderName;

        /**
         * Creates a tuple joining
         *
         * @param securityTokenName the token name
         * @param xsrfCookieName    the xsrf cookie name
         * @param xsrfHeaderName    the xsrf header/parameter name
         */
        public SecurityTokenCookieParameterNameTuple(String securityTokenName,
                                                     String xsrfCookieName,
                                                     String xsrfHeaderName) {
            this.xsrfCookieName = xsrfCookieName;
            this.xsrfHeaderName = xsrfHeaderName;
            this.securityTokenName = securityTokenName;
        }
    }
}
