package com.emmisolutions.emmimanager.web.rest.admin.security.cas;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Returns a 403 error code (Forbidden) to the client, specialized for CAS requests.
 */
@Component
public class CasAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        response.sendError(HttpServletResponse.SC_FORBIDDEN, exception.getClass().getSimpleName());

    }
}
