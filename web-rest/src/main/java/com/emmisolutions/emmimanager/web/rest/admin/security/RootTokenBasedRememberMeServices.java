package com.emmisolutions.emmimanager.web.rest.admin.security;

import com.emmisolutions.emmimanager.model.configuration.ClientPasswordConfiguration;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.service.ClientPasswordConfigurationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Remember me services that use a cookie at the root
 */
public class RootTokenBasedRememberMeServices extends TokenBasedRememberMeServices {

    private Method setHttpOnlyMethod;

    private boolean useSessionCookieOnly;

    @Resource
    ClientPasswordConfigurationService clientPasswordConfigurationService;

    /**
     * Creates the service
     *
     * @param key                the key
     * @param userDetailsService to load authenticated users
     */
    public RootTokenBasedRememberMeServices(String key, UserDetailsService userDetailsService) {
        super(key, userDetailsService);
        this.setHttpOnlyMethod = ReflectionUtils.findMethod(Cookie.class, "setHttpOnly", boolean.class);
    }

    @Override
    public String extractRememberMeCookie(HttpServletRequest request) {
        return super.extractRememberMeCookie(request);
    }

    @Override
    protected int calculateLoginLifetime(HttpServletRequest request, Authentication authentication) {
        String paramValue = request.getParameter(getParameter());
        Object authPrincipal = authentication.getPrincipal();
        int validity = 15 * 60;

        if (authPrincipal instanceof UserClient) {
            // use the configured client level idle time for UserClients
            ClientPasswordConfiguration clientPasswordConfiguration =
                    clientPasswordConfigurationService.get(((UserClient) authPrincipal).getClient());
            validity = clientPasswordConfiguration.getIdleTime() * 60;
        }

        if (paramValue != null) {
            // if the user has checked the 'remember-me' field change timeout to configured validity (e.g. one month)
            if (paramValue.equalsIgnoreCase("true") || paramValue.equalsIgnoreCase("on") ||
                    paramValue.equalsIgnoreCase("yes") || paramValue.equals("1")) {
                validity = getTokenValiditySeconds();
            }
        }

        if (useSessionCookieOnly) {
            // this should be a session based cookie, use the configured timeout (internally)
            validity = getTokenValiditySeconds();
        }

        return validity;
    }

    @Override
    protected void setCookie(String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response) {
        String cookieValue = encodeCookie(tokens);
        Cookie cookie = new Cookie(getCookieName(), cookieValue);

        if (useSessionCookieOnly) {
            cookie.setMaxAge(-1);
        } else {
            cookie.setMaxAge(maxAge);
        }

        cookie.setPath("/"); // need to set at root of site

        // set secure flag if ssl is in use via a proxy or directly
        cookie.setSecure(request.isSecure() ||
                StringUtils.equalsIgnoreCase(request.getHeader("X-Forwarded-Ssl"), "on"));

        if (setHttpOnlyMethod != null) {
            ReflectionUtils.invokeMethod(setHttpOnlyMethod, cookie, Boolean.TRUE);
        } else if (logger.isDebugEnabled()) {
            logger.debug("Note: Cookie will not be marked as HttpOnly because you are not using Servlet 3.0 (Cookie#setHttpOnly(boolean) was not found).");
        }

        response.addCookie(cookie);
    }

    @Override
    protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request, HttpServletResponse response) {
        final UserDetails ret = super.processAutoLoginCookie(cookieTokens, request, response);
        // re-write the cookie on each request (which is a login due to stateless back-end) for new timestamp
        onLoginSuccess(request, response, new RememberMeAuthenticationToken(getKey(), ret, ret.getAuthorities()));
        return ret;
    }

    @Override
    protected void cancelCookie(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("Cancelling cookie");
        Cookie cookie = new Cookie(getCookieName(), null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public void setUseSessionCookieOnly(boolean useSessionCookieOnly) {
        this.useSessionCookieOnly = useSessionCookieOnly;
    }
}
