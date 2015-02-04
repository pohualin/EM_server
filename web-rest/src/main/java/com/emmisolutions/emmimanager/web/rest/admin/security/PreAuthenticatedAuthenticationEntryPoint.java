package com.emmisolutions.emmimanager.web.rest.admin.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Returns a 401 error code (Unauthorized) to the client. This means that the clients will have to have been
 * pre-authenticated when this entry point is used. We register this entry point for AJAX requests inside
 * the SecurityConfiguration.
 *
 * @see com.emmisolutions.emmimanager.web.rest.admin.configuration.SecurityConfiguration
 */
@Component
public class PreAuthenticatedAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Always returns a 401 error code to the client.
     *
     * @param request  the request
     * @param response the response
     * @param arg2     auth exception
     * @throws IOException      if there's a problem
     * @throws ServletException if there's a problem
     */
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException arg2) throws IOException,
            ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, arg2.getClass().getSimpleName());
    }

}
