package com.emmisolutions.emmimanager.web.rest.admin.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Depending upon the cookie name present in the request, switch to the appropriate remember me services implementation.
 */
public class DelegateRememberMeServices implements RememberMeServices, LogoutHandler {

    @Resource(name = "clientTokenBasedRememberMeServices")
    RootTokenBasedRememberMeServices clientRootTokenBasedRememberMeServices;

    @Resource(name = "impersonationTokenBasedRememberMeServices")
    RootTokenBasedRememberMeServices impersonationTokenBasedRememberMeServices;

    @Override
    public Authentication autoLogin(HttpServletRequest request, HttpServletResponse response) {
        return delegate(request).autoLogin(request, response);
    }

    @Override
    public void loginFail(HttpServletRequest request, HttpServletResponse response) {
        delegate(request).loginFail(request, response);
    }

    @Override
    public void loginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
        delegate(request).loginSuccess(request, response, successfulAuthentication);
    }


    private TokenBasedRememberMeServices delegate(HttpServletRequest request) {
        TokenBasedRememberMeServices ret;
        if (StringUtils.isNotBlank(impersonationTokenBasedRememberMeServices.extractRememberMeCookie(request))) {
            ret = impersonationTokenBasedRememberMeServices;
        } else {
            ret = clientRootTokenBasedRememberMeServices;
        }
        return ret;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        impersonationTokenBasedRememberMeServices.logout(request, response, authentication);
        clientRootTokenBasedRememberMeServices.logout(request, response, authentication);
    }
}
