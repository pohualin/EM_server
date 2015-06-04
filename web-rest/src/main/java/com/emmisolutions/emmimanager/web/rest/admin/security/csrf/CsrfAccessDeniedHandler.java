package com.emmisolutions.emmimanager.web.rest.admin.security.csrf;

import com.emmisolutions.emmimanager.web.rest.client.model.security.UserClientLoginError;
import com.emmisolutions.emmimanager.web.rest.client.model.security.UserClientLoginErrorResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.csrf.CsrfException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Pushes XSRF errors into our existing client login failure handlers so
 * the front end can handle this situation properly
 */
@Component("clientCsrfAccessDeniedHandler")
public class CsrfAccessDeniedHandler extends AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Resource(name = "userClientLoginErrorResourceAssembler")
    private ResourceAssembler<UserClientLoginError, UserClientLoginErrorResource>
            userClientLoginFailureResourceAssembler;

    @Resource
    MappingJackson2HttpMessageConverter jsonJacksonConverter;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        if (accessDeniedException instanceof CsrfException) {
            UserClientLoginError failure;
            if (accessDeniedException instanceof MissingCsrfTokenException) {
                failure = new UserClientLoginError(UserClientLoginError.Reason.XSRF_MISSING);
            } else {
                failure = new UserClientLoginError(UserClientLoginError.Reason.XSRF_INVALID);
            }

            UserClientLoginErrorResource resource = userClientLoginFailureResourceAssembler
                    .toResource(failure);

            if (resource != null) {
                String loginError = jsonJacksonConverter.getObjectMapper()
                        .writeValueAsString(resource);
                request.setAttribute("loginError", loginError);
                request.setAttribute("exception", loginError);
            }

            // send back a 401, which will go to the login-expired.jsp page for output
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    accessDeniedException.getMessage());
            return;
        }

        super.handle(request, response, accessDeniedException);

    }
}
