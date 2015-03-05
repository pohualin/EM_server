package com.emmisolutions.emmimanager.web.rest.client.configuration.security;


import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.service.UserClientService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Check to see if the passed password matches the logged in user.
 */
@Component("password")
public class UserKnowsPasswordAuthorizationRequest {

    @Resource
    UserClientService userClientService;

    @Resource
    PasswordEncoder passwordEncoder;

    /**
     * Check the passed password against the authentication
     *
     * @param passwordObject plain text password
     * @param authentication the authenticated user
     * @return true if password matches, false if it doesn't
     */
    public boolean isLoggedInAndKnowsPassword(Object passwordObject, Authentication authentication) {
        if (passwordObject != null) {
            UserClient userClient = userClientService.reload((UserClient) authentication.getPrincipal());
            if (userClient != null) {
                return passwordEncoder.matches(
                        passwordObject.toString(),
                        userClient.getPassword() + userClient.getSalt());
            }
        }
        return false;
    }

}
