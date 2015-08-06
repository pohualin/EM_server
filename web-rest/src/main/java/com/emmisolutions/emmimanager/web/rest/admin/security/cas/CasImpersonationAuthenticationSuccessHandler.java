package com.emmisolutions.emmimanager.web.rest.admin.security.cas;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.service.audit.AuthenticationAuditService;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.emmisolutions.emmimanager.service.audit.AuthenticationAuditService.APPLICATION.CLIENT_FACING;

/**
 * Spring Security success handler, specialized for CAS requests using impersonation.
 */
@Component("casImpersonationAuthenticationSuccessHandler")
@Profile(value = {"cas", "prod"})
public class CasImpersonationAuthenticationSuccessHandler extends CasAuthenticationSuccessHandler {

    @Resource(name = "impersonationTokenBasedRememberMeServices")
    TokenBasedRememberMeServices impersonationTokenBasedRememberMeServices;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        /*
            do NOT log success here due to the fact that the true actual login will happen
            later when the super method is called

            i.e. don't explicitly call authenticationLoggingUtility.login(authentication, SUCCESS, CLIENT_FACING);
         */

        if (authentication.getPrincipal() instanceof UserClient && ((UserClient) authentication.getPrincipal()).isImpersonated()) {
            impersonationTokenBasedRememberMeServices.onLoginSuccess(request, response, authentication);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }

    @Override
    protected AuthenticationAuditService.APPLICATION getApplication() {
        // impersonation takes you to the client facing application
        return CLIENT_FACING;
    }

}
