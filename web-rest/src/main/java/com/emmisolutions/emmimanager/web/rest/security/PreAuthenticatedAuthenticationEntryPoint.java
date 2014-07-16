package com.emmisolutions.emmimanager.web.rest.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @see com.emmisolutions.emmimanager.web.rest.configuration.SecurityConfiguration
 */
@Component
public class PreAuthenticatedAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final Logger log = LoggerFactory.getLogger(PreAuthenticatedAuthenticationEntryPoint.class);

    /**
     * Always returns a 401 error code to the client.
     */
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException arg2) throws IOException,
            ServletException {
            log.debug("Pre-authenticated entry point called. Rejecting access");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied");
    }

}
