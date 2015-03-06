package com.emmisolutions.emmimanager.web.rest.client.configuration.security;


import com.emmisolutions.emmimanager.model.user.client.UserClient;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Check to see if the requested user id is the same as the logged in user
 */
@Component("user")
public class UserAuthorizationRequest {

    /**
     * Ensure user id is the same as the logged in user
     *
     * @param userIdInRequest the user in the request
     * @param authentication  the logged in user
     * @return true if they match, false if otherwise
     */
    public boolean isLoggedIn(Object userIdInRequest, Authentication authentication) {
        if (userIdInRequest != null && authentication != null && authentication.getPrincipal() != null) {
            return userIdInRequest.equals(((UserClient) authentication.getPrincipal()).getId());
        }
        return false;
    }
}
