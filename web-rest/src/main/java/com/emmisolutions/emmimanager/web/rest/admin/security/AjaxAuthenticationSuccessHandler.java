package com.emmisolutions.emmimanager.web.rest.admin.security;

import com.emmisolutions.emmimanager.web.rest.client.configuration.audit.AuthenticationLoggingUtility;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.emmisolutions.emmimanager.model.audit.login.LoginStatusName.SUCCESS;
import static com.emmisolutions.emmimanager.service.audit.AuthenticationAuditService.APPLICATION.ADMIN_FACING;

/**
 * Spring Security success handler, specialized for Ajax requests. This class is called
 * when a user logs in to the application via the login form instead of via CAS.
 */
@Component
public class AjaxAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Resource
    AuthenticationLoggingUtility authenticationLoggingUtility;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        authenticationLoggingUtility.login(authentication, SUCCESS, ADMIN_FACING);

        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }

    }
}
