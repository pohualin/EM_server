package com.emmisolutions.emmimanager.web.rest.admin.security;

import com.emmisolutions.emmimanager.model.audit.login.LoginStatusName;
import com.emmisolutions.emmimanager.web.rest.client.configuration.audit.AuthenticationLoggingUtility;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.emmisolutions.emmimanager.model.audit.login.LoginStatusName.INACTIVE;
import static com.emmisolutions.emmimanager.model.audit.login.LoginStatusName.INVALID_CREDENTIALS;
import static com.emmisolutions.emmimanager.service.audit.AuthenticationAuditService.APPLICATION.ADMIN_FACING;

/**
 * Returns a 401 error code (Unauthorized) to the client, specialized for Ajax requests.
 */
@Component
public class AjaxAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Resource
    AuthenticationLoggingUtility authenticationLoggingUtility;

    @Override
    @SuppressWarnings("deprecation")
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        LoginStatusName status = INVALID_CREDENTIALS;
        if (exception instanceof DisabledException) {
            status = INACTIVE;
        }
        authenticationLoggingUtility.login(exception.getAuthentication(), status, ADMIN_FACING);

        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getClass().getSimpleName());
        } else {
            super.onAuthenticationFailure(request, response, exception);
        }

    }
}
