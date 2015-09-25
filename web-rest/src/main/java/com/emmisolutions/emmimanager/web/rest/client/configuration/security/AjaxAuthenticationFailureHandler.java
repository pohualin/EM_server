package com.emmisolutions.emmimanager.web.rest.client.configuration.security;

import com.emmisolutions.emmimanager.exception.IpAddressAuthenticationException;
import com.emmisolutions.emmimanager.model.audit.login.LoginStatusName;
import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.service.UserClientService;
import com.emmisolutions.emmimanager.service.security.UserDetailsService;
import com.emmisolutions.emmimanager.web.rest.client.configuration.audit.AuthenticationLoggingUtility;
import com.emmisolutions.emmimanager.web.rest.client.model.security.UserClientLoginError;
import com.emmisolutions.emmimanager.web.rest.client.model.security.UserClientLoginErrorResource;
import com.emmisolutions.emmimanager.web.rest.client.model.security.UserClientLoginErrorResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.client.model.user.UserClientResource;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.emmisolutions.emmimanager.service.audit.AuthenticationAuditService.APPLICATION.CLIENT_FACING;

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

    @Resource
    AuthenticationLoggingUtility authenticationLoggingUtility;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        // put the exception into the request
        request.setAttribute("exception", exception);

        // add a more specific error as a JSON string, log the failure
        addJsonErrorToRequest(request, exception);

        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            // send back a 401, which will go to the login-expired.jsp page for output
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    exception.getMessage());
        } else {
            // not ajax, do what we normally do
            super.onAuthenticationFailure(request, response, exception);
        }

    }

    @SuppressWarnings("deprecation")
    private void addJsonErrorToRequest(HttpServletRequest request, AuthenticationException exception)
            throws JsonProcessingException {

        UserClientLoginError failure = null;
        if (exception instanceof CredentialsExpiredException
                && exception.getExtraInformation() instanceof User) {
            UserClient userClient = (UserClient) userDetailsService
                    .loadUserByUsername((String) exception
                            .getAuthentication().getPrincipal());
            userClient = userClientService.reload(userClient);
            boolean canChange = userClient.getPasswordResetExpirationDateTime() != null &&
                    LocalDateTime.now(DateTimeZone.UTC).isBefore(userClient.getPasswordResetExpirationDateTime());
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
        } else if (exception instanceof IpAddressAuthenticationException) {
            failure = new UserClientLoginError(
                    UserClientLoginError.Reason.INVALID_IP_ADDRESS);
        } else if (exception instanceof DisabledException) {
            failure = new UserClientLoginError(
                    UserClientLoginError.Reason.BAD);
        }

        UserClientLoginErrorResource resource = userClientLoginFailureResourceAssembler
                .toResource(failure);

        if (resource != null) {
            String loginError = jsonJacksonConverter.getObjectMapper()
                    .writeValueAsString(resource);
            request.setAttribute("loginError", loginError);
        }

        recordFailure(exception.getAuthentication(), failure);
    }

    private void recordFailure(Authentication authentication, UserClientLoginError failure) {
        LoginStatusName status = null;
        switch (failure.getReason()) {
            case BAD:
                status = LoginStatusName.INVALID_CREDENTIALS;
                break;
            case EXPIRED:
                status = LoginStatusName.EXPIRED;
                break;
            case EXPIRED_CANT_CHANGE:
                status = LoginStatusName.EXPIRED_CANT_CHANGE;
                break;
            case LOCK:
                status = LoginStatusName.LOCKED_OUT;
                break;
            case INVALID_IP_ADDRESS:
                status = LoginStatusName.INVALID_IP_ADDRESS;
                break;
        }
        if (status != null) {
            authenticationLoggingUtility.login(authentication, status, CLIENT_FACING);
        }
    }
}

