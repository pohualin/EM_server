package com.emmisolutions.emmimanager.web.rest.client.configuration.security;


import com.emmisolutions.emmimanager.model.user.client.UserClient;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Check to see if a client level permission is present in the authorities.
 */
@Component("user")
public class UserAuthorizationRequest  {

    private Long userId;

    private UserAuthorizationRequest() {
    }

    private UserAuthorizationRequest(Long userId) {
        this.userId = userId;
    }

    /**
     * Make a new ClientAuthorizationRequest instance.
     * This method is named this way to make SPeL code look good.
     *
     * @param userId the client id
     * @return a new instance of this class
     */
    public UserAuthorizationRequest id(Long userId) {
        return new UserAuthorizationRequest(userId);
    }

    public boolean isLoggedIn(Authentication authentication ){
        return this.userId.equals(((UserClient)authentication.getPrincipal()).getId());
    }
}
