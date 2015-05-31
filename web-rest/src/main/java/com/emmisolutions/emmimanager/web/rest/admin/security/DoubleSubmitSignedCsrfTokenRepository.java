package com.emmisolutions.emmimanager.web.rest.admin.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * The CSRF token repository implementation. This is essentially
 * a double submit solution. This means that both the request
 * parameter and cookie value must match in order for the request
 * to be valid. It is also a secure implementation in that the security
 * token (used to authenticate the user) is used to 'sign' the generated
 * csrf token.
 * <p/>
 * By default the xsrf cookie name and xsrf parameter name will work
 * with angular $http services out of the box.
 */
public class DoubleSubmitSignedCsrfTokenRepository implements CsrfTokenRepository {

    private String securityTokenName;
    private String xsrfCookieName = "XSRF-TOKEN";
    private String xsrfParameterName = "X-XSRF-TOKEN";
    private String DELIMITER = "-";
    /**
     * the server only secret string ensures that even if a client can read the security token,
     * they still won't be able to create a valid signature, unless of course they know this
     * secret string and the algorithm used for the hash
     */
    private String secretSalt = "server||only||secret||";

    /**
     * Create a new repository
     *
     * @param securityTokenName this is the name of the cookie, request parameter or
     *                          request header where the user's authentication is stored
     */
    public DoubleSubmitSignedCsrfTokenRepository(String securityTokenName) {
        this.securityTokenName = securityTokenName;
    }

    @Override
    public CsrfToken generateToken(HttpServletRequest request) {
        return new DefaultCsrfToken(xsrfParameterName, xsrfParameterName, sign(UUID.randomUUID().toString(), request));
    }

    @Override
    public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
        if (token != null) {
            Cookie csrfCookie = new Cookie(xsrfCookieName, token.getToken());
            csrfCookie.setPath("/");
            csrfCookie.setMaxAge(-1);
            // set secure flag if ssl is in use via a proxy or directly
            csrfCookie.setSecure(request.isSecure() ||
                    StringUtils.equalsIgnoreCase(request.getHeader("X-Forwarded-Ssl"), "on"));
            response.addCookie(csrfCookie);
        }
    }

    @Override
    public CsrfToken loadToken(HttpServletRequest request) {
        String csrfFromCookie = "000"; // never return null from this method
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equalsIgnoreCase(xsrfCookieName)) {
                    csrfFromCookie = cookie.getValue();
                }
            }
        }
        return new DefaultCsrfToken(xsrfParameterName, xsrfParameterName, csrfFromCookie);
    }

    /**
     * Signs the passed string.
     *
     * @param toAppendTo to be signed
     * @param request    where to look for the security token
     * @return signed string in the form of toAppendTo::theHash
     */
    private String sign(String toAppendTo, HttpServletRequest request) {
        StringBuilder csrfToken = new StringBuilder(toAppendTo);
        StringBuilder toHash = new StringBuilder(secretSalt);
        boolean cookieFound = false;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (StringUtils.equalsIgnoreCase(cookie.getName(), securityTokenName)) {
                    toHash.append(cookie.getValue());
                    cookieFound = true;
                    break;
                }
            }
        }
        if (!cookieFound) {
            // look for a request parameter or request header for the security token
            String securityToken = request.getParameter(securityTokenName);
            if (StringUtils.isBlank(securityToken)) {
                securityToken = request.getHeader(securityTokenName);
            }
            toHash.append(securityToken);
        }
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No MD5 algorithm available!");
        }
        return csrfToken.append(DELIMITER)
                .append(new String(Hex.encode(digest.digest(toHash.toString().getBytes()))))
                .toString();
    }

    public String getXsrfCookieName() {
        return xsrfCookieName;
    }

    public void setXsrfCookieName(String xsrfCookieName) {
        this.xsrfCookieName = xsrfCookieName;
    }

    public String getXsrfParameterName() {
        return xsrfParameterName;
    }

    public void setXsrfParameterName(String xsrfParameterName) {
        this.xsrfParameterName = xsrfParameterName;
    }

    public String getSecretSalt() {
        return secretSalt;
    }

    public void setSecretSalt(String secretSalt) {
        this.secretSalt = secretSalt;
    }
}
