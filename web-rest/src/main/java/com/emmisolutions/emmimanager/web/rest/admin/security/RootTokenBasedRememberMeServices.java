package com.emmisolutions.emmimanager.web.rest.admin.security;

import com.emmisolutions.emmimanager.model.configuration.ClientPasswordConfiguration;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.service.ClientPasswordConfigurationService;
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

    private Boolean useSecureCookie = null;

    private Method setHttpOnlyMethod;

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
    public void setUseSecureCookie(boolean useSecureCookie) {
        this.useSecureCookie = useSecureCookie;
    }

    @Override
    protected int calculateLoginLifetime(HttpServletRequest request, Authentication authentication) {
        String paramValue = request.getParameter(getParameter());
        Object authPrincipal = authentication.getPrincipal();
        int validity = 15 * 60;
        if (authPrincipal instanceof UserClient){
            ClientPasswordConfiguration clientPasswordConfiguration =
                    clientPasswordConfigurationService.get(((UserClient) authPrincipal).getClient());
            validity = clientPasswordConfiguration.getIdleTime() * 60;
        }

        if (paramValue != null) {
            if (paramValue.equalsIgnoreCase("true") || paramValue.equalsIgnoreCase("on") ||
                    paramValue.equalsIgnoreCase("yes") || paramValue.equals("1")) {
                validity = getTokenValiditySeconds();
            }
        }
        return validity;
    }

    @Override
    protected void setCookie(String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response) {
        String cookieValue = encodeCookie(tokens);
        Cookie cookie = new Cookie(getCookieName(), cookieValue);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");

        if (useSecureCookie == null) {
            cookie.setSecure(request.isSecure());
        } else {
            cookie.setSecure(useSecureCookie);
        }

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


}
