package com.emmisolutions.emmimanager.service.security;

import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.model.user.client.UserClient;

import java.io.Serializable;

/**
 * Service which allows us to work with User/UserDetails.
 */
public interface UserDetailsService extends org.springframework.security.core.userdetails.UserDetailsService, Serializable {

    /**
     * Fetches the currently logged in user.
     *
     * @return the currently logged in user or null if nobody logged in
     */
    User getLoggedInUser();
    
    /**
     * Fetches a given user client.
     * 
     * @param userClient
     * @return
     */
    User get(UserClient userClient);

}
