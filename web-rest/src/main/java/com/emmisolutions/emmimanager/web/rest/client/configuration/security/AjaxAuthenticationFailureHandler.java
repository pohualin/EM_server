package com.emmisolutions.emmimanager.web.rest.client.configuration.security;

import com.emmisolutions.emmimanager.model.configuration.ClientPasswordConfiguration;
import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.service.ClientPasswordConfigurationService;
import com.emmisolutions.emmimanager.service.UserClientService;
import com.emmisolutions.emmimanager.service.security.UserDetailsService;
import com.emmisolutions.emmimanager.web.rest.admin.model.configuration.ClientPasswordConfigurationResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.client.model.security.UserClientLoginError;
import com.emmisolutions.emmimanager.web.rest.client.model.security.UserClientLoginErrorResource;
import com.emmisolutions.emmimanager.web.rest.client.model.security.UserClientLoginErrorResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.client.model.user.UserClientResource;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Returns a 401 error code (Unauthorized) to the client, specialized for Ajax requests.
 */
@Component("clientAjaxAuthenticationFailureHandler")
public class AjaxAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Resource
    ClientPasswordConfigurationService clientPasswordConfigurationService;
    
    @Resource
    UserClientService userClientService;
    
    @Resource(name = "clientUserDetailsService")
    UserDetailsService userDetailsService;
    
    @Resource(name = "userClientAuthenticationResourceAssembler")
    ResourceAssembler<User, UserClientResource> userUserClientResourceResourceAssembler;
    
    @Resource
    ClientPasswordConfigurationResourceAssembler clientPasswordConfigurationResourceAssembler;

    @Resource
    UserClientLoginErrorResourceAssembler userClientLoginFailureResourceAssembler;
    
    @Resource
    MappingJackson2HttpMessageConverter jsonJacksonConverter;

    @Override
    @SuppressWarnings("deprecation")
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        // put the exception into the request
        request.setAttribute("exception", exception);

        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            // handle ajax error
            String client = null;
            if (exception instanceof CredentialsExpiredException &&
                    exception.getExtraInformation() instanceof User) {
                // serialize the client resource as a json string
                UserClientResource userClientResource =
                        userUserClientResourceResourceAssembler.toResource((User) exception.getExtraInformation());
                client = jsonJacksonConverter.getObjectMapper().writeValueAsString(
                        userClientResource.getClientResource());
            } else if (exception instanceof BadCredentialsException) {
                UserClient userClient = (UserClient) userDetailsService
                        .loadUserByUsername((String) exception
                                .getAuthentication().getPrincipal());
                userClient = userClientService.handleLoginFailure(userClient);
                ClientPasswordConfiguration configuration = clientPasswordConfigurationService
                        .get(userClient.getClient());

                UserClientLoginError failure = new UserClientLoginError(
                        UserClientLoginError.Reason.BAD_CREDENTIAL,
                        userClient, configuration);
                UserClientLoginErrorResource resource = userClientLoginFailureResourceAssembler
                        .toResource(failure);
                String loginError = jsonJacksonConverter.getObjectMapper()
                        .writeValueAsString(resource);
                request.setAttribute("loginError", loginError);
            } else if (exception instanceof LockedException) {
                UserClient userClient = (UserClient) userDetailsService
                        .loadUserByUsername((String) exception
                                .getAuthentication().getPrincipal());
                userClient = userClientService.reload(userClient);
                ClientPasswordConfiguration configuration = clientPasswordConfigurationService
                        .get(userClient.getClient());
                UserClientLoginError failure = new UserClientLoginError(
                        UserClientLoginError.Reason.LOCK,
                        userClient, configuration);
                UserClientLoginErrorResource resource = userClientLoginFailureResourceAssembler
                        .toResource(failure);
                String loginError = jsonJacksonConverter.getObjectMapper()
                        .writeValueAsString(resource);
                request.setAttribute("loginError", loginError);
            }
            // push the client into the request (it'll get picked up by login-expired.jsp)
            request.setAttribute("client", client);
            // send back a 401, which will go to the login-expired.jsp page for output
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
        } else {
            // not ajax, do what we normally do
            super.onAuthenticationFailure(request, response, exception);
        }

    }
}
