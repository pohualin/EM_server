package com.emmisolutions.emmimanager.web.rest.client.configuration.security;

import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.service.UserClientService;
import com.emmisolutions.emmimanager.service.security.UserDetailsService;
import com.emmisolutions.emmimanager.web.rest.client.model.security.UserClientLoginError;
import com.emmisolutions.emmimanager.web.rest.client.model.security.UserClientLoginErrorResource;
import com.emmisolutions.emmimanager.web.rest.client.model.security.UserClientLoginErrorResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.client.model.user.UserClientResource;
import org.joda.time.LocalDateTime;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Returns a 401 error code (Unauthorized) to the client, specialized for Ajax
 * requests.
 */
@Component("clientAjaxAuthenticationFailureHandler")
public class AjaxAuthenticationFailureHandler extends
        SimpleUrlAuthenticationFailureHandler {

    @Resource
    UserClientService userClientService;

    @Resource(name = "clientUserDetailsService")
    UserDetailsService userDetailsService;

    @Resource(name = "userClientAuthenticationResourceAssembler")
    ResourceAssembler<UserClient, UserClientResource> userUserClientResourceResourceAssembler;

    @Resource
    UserClientLoginErrorResourceAssembler userClientLoginFailureResourceAssembler;

    @Resource
    MappingJackson2HttpMessageConverter jsonJacksonConverter;

    @Override
    @SuppressWarnings("deprecation")
    public void onAuthenticationFailure(HttpServletRequest request,
            HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        // put the exception into the request
        request.setAttribute("exception", exception);

        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            // handle ajax error
            UserClientLoginError failure = null;
            if (exception instanceof CredentialsExpiredException
                    && exception.getExtraInformation() instanceof User) {
                UserClient userClient = (UserClient) userDetailsService
                        .loadUserByUsername((String) exception
                                .getAuthentication().getPrincipal());
                userClient = userClientService.reload(userClient);
                boolean canChange = userClient.getPasswordResetExpirationDateTime() != null &&
                        LocalDateTime.now().isBefore(userClient.getPasswordResetExpirationDateTime());
                failure = new UserClientLoginError(
                        (canChange) ? UserClientLoginError.Reason.EXPIRED : UserClientLoginError.Reason.EXPIRED_CANT_CHANGE,
                        userClient);
            } else if (exception instanceof BadCredentialsException) {
                try {
                    UserClient userClient = (UserClient) userDetailsService
                            .loadUserByUsername((String) exception
                                    .getAuthentication().getPrincipal());
                    userClient = userClientService
                            .handleLoginFailure(userClient);
                    if (userClient.isAccountNonLocked()) {
                        failure = new UserClientLoginError(
                                UserClientLoginError.Reason.BAD, userClient);
                    } else {
                        failure = new UserClientLoginError(
                                UserClientLoginError.Reason.LOCK, userClient);
                    }
                } catch (UsernameNotFoundException e) {
                    failure = new UserClientLoginError(
                            UserClientLoginError.Reason.BAD);
                }
            } else if (exception instanceof LockedException) {
                UserClient userClient = (UserClient) userDetailsService
                        .loadUserByUsername((String) exception
                                .getAuthentication().getPrincipal());
                userClient = userClientService.reload(userClient);
                failure = new UserClientLoginError(
                        UserClientLoginError.Reason.LOCK, userClient);
            }

            UserClientLoginErrorResource resource = userClientLoginFailureResourceAssembler
                    .toResource(failure);

            if (resource != null) {
                String loginError = jsonJacksonConverter.getObjectMapper()
                        .writeValueAsString(resource);
                request.setAttribute("loginError", loginError);
            }

            // send back a 401, which will go to the login-expired.jsp page for
            // output
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    exception.getMessage());
        } else {
            // not ajax, do what we normally do
            super.onAuthenticationFailure(request, response, exception);
        }

    }
}
