package com.emmisolutions.emmimanager.web.rest.admin.security.cas;

import com.emmisolutions.emmimanager.model.user.client.UserClient;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Spring Security success handler, specialized for CAS requests using impersonation.
 */
@Component("casImpersonationAuthenticationSuccessHandler")
@Profile(value = {"cas", "prod"})
public class CasImpersonationAuthenticationSuccessHandler extends CasAuthenticationSuccessHandler {

    @Resource(name ="impersonationTokenBasedRememberMeServices")
    TokenBasedRememberMeServices impersonationTokenBasedRememberMeServices;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        if (authentication.getPrincipal() instanceof UserClient && ((UserClient) authentication.getPrincipal()).isImpersonated()) {
            impersonationTokenBasedRememberMeServices.onLoginSuccess(request, response, authentication);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }


}
