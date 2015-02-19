package com.emmisolutions.emmimanager.web.rest.admin.security.cas;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Spring Security success handler, specialized for CAS requests.
 */
@Component
public class CasAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

            if (request.getSession() != null) {
                String requestedUrl = (String) request.getSession().getAttribute("X-Requested-Url");
                if (StringUtils.isNotBlank(requestedUrl)) {
                    response.sendRedirect(requestedUrl);
                    return;
                }
            }
            super.onAuthenticationSuccess(request, response, authentication);
    }
}
