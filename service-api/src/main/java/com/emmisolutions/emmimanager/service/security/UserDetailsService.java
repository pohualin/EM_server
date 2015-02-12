package com.emmisolutions.emmimanager.service.security;

import com.emmisolutions.emmimanager.model.user.User;

/**
 * Service which allows us to work with User/UserDetails.
 */
public interface UserDetailsService extends org.springframework.security.core.userdetails.UserDetailsService {

    /**
     * Fetches the currently logged in user.
     *
     * @return the currently logged in user or null if nobody logged in
     */
    User getLoggedInUser();
}