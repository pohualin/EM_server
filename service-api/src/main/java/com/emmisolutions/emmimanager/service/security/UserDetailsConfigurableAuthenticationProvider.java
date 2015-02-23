package com.emmisolutions.emmimanager.service.security;

import org.springframework.security.authentication.AuthenticationProvider;

/**
 * Allow for the user details service to be injected into the provider
 */
public interface UserDetailsConfigurableAuthenticationProvider extends AuthenticationProvider {

    /**
     * Sets the user details service to use after successful login
     *
     * @param userDetailsService to lookup the user
     */
    public void setUserDetailsService(UserDetailsService userDetailsService);
}
