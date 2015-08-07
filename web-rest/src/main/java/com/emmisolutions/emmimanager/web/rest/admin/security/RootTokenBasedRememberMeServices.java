package com.emmisolutions.emmimanager.web.rest.admin.security;

import com.emmisolutions.emmimanager.model.configuration.ClientPasswordConfiguration;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.service.ClientPasswordConfigurationService;
import com.emmisolutions.emmimanager.service.audit.AuthenticationAuditService;
import com.emmisolutions.emmimanager.web.rest.client.configuration.audit.AuthenticationLoggingUtility;
import com.emmisolutions.emmimanager.web.rest.client.configuration.audit.HttpProxyAwareAuthenticationDetailsImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

import static com.emmisolutions.emmimanager.model.audit.logout.LogoutSourceName.TIMEOUT;
import static com.emmisolutions.emmimanager.model.audit.logout.LogoutSourceName.USER;

/**
 * Remember me services that use a cookie at the root
 */
public class RootTokenBasedRememberMeServices extends TokenBasedRememberMeServices {

    @Resource
    ClientPasswordConfigurationService clientPasswordConfigurationService;
    @Resource
    AuthenticationLoggingUtility authenticationLoggingUtility;
    private Method setHttpOnlyMethod;
    private boolean useSessionCookieOnly;
    private AuthenticationAuditService.APPLICATION application;

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
        Object authPrincipal = authentication.getPrincipal();
        int validity = 15 * 60;

        if (authPrincipal instanceof UserClient) {
            // use the configured client level idle time for UserClients
            ClientPasswordConfiguration clientPasswordConfiguration =
                    clientPasswordConfigurationService.get(((UserClient) authPrincipal).getClient());
            validity = clientPasswordConfiguration.getIdleTime() * 60;
        }

        if (useSessionCookieOnly) {
            // for session cookies make them valid for a long time (defaults to two weeks)
            validity = getTokenValiditySeconds();
        }

        return validity;
    }

    @Override
    protected void setCookie(String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response) {
        String cookieValue = encodeCookie(tokens);
        Cookie cookie = new Cookie(getCookieName(), cookieValue);

        // always use session cookies for the authentication token
        cookie.setMaxAge(-1);

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
        try {
            final UserDetails ret = super.processAutoLoginCookie(cookieTokens, request, response);
            // re-write the cookie on each request (which is a login due to stateless back-end) for new timestamp
            onLoginSuccess(request, response, new RememberMeAuthenticationToken(getKey(), ret, ret.getAuthorities()));
            return ret;
        } catch (InvalidCookieException e) {
            // log a timeout if the cookie is invalid but we can discern who the user is
            final UserDetails userDetails = getUserDetailsService().loadUserByUsername(cookieTokens[0]);
            if (userDetails != null) {
                RememberMeAuthenticationToken token =
                        new RememberMeAuthenticationToken(getKey(), userDetails, userDetails.getAuthorities());
                token.setDetails(new HttpProxyAwareAuthenticationDetailsImpl(request));
                authenticationLoggingUtility.logout(token, TIMEOUT, application);
            }
            throw e;
        }
    }

    /**
     * Use this method to rewrite the login/authentication token when the user client has changed the
     * password or login name.
     *
     * @param request    the servlet request
     * @param response   the servlet response
     * @param userClient the changed User Client
     */
    public void rewriteLoginToken(HttpServletRequest request, HttpServletResponse response, UserClient userClient) {
        onLoginSuccess(request, response, new RememberMeAuthenticationToken(getKey(), userClient,
                SecurityContextHolder.getContext().getAuthentication().getAuthorities()));
    }

    @Override
    protected void cancelCookie(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("Cancelling cookie");
        Cookie cookie = new Cookie(getCookieName(), null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // re-authenticate the user so we can record the logout
        Authentication userLoggingOut = autoLogin(request, response);
        if (userLoggingOut != null) {
            authenticationLoggingUtility.logout(userLoggingOut, USER, application);
        }
        super.logout(request, response, authentication);
    }

    public void setUseSessionCookieOnly(boolean useSessionCookieOnly) {
        this.useSessionCookieOnly = useSessionCookieOnly;
    }

    public void setApplication(AuthenticationAuditService.APPLICATION application) {
        this.application = application;
    }
}
