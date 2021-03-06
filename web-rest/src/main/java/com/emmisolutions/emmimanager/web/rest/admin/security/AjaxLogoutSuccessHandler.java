package com.emmisolutions.emmimanager.web.rest.admin.security;

import com.emmisolutions.emmimanager.web.rest.admin.security.cas.SavedUrlServiceAuthenticationDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Spring Security logout handler, specialized for Ajax requests.
 */
@Component
public class AjaxLogoutSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler
        implements LogoutSuccessHandler {

    @Value("${cas.server.logout.url:https://devcas1.emmisolutions.com/cas/logout}")
    private String casServerLogoutUrl;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication)
            throws IOException, ServletException {

        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            // ajax should always return OK
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            // non-ajax
            if (authentication != null && authentication.getDetails() instanceof SavedUrlServiceAuthenticationDetails) {
                // cas authenticated, redirect to cas logout url
                response.sendRedirect(casServerLogoutUrl);
            } else {
                // not cas authenticated, send OK
                response.setStatus(HttpServletResponse.SC_OK);
            }
        }

    }
}
