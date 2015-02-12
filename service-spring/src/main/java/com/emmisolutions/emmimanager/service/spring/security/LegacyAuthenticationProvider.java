package com.emmisolutions.emmimanager.service.spring.security;

import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.persistence.UserAdminPersistence;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Replacement for the DaoAuthentication provider to support legacy emmi passwords.
 * This class supports Emmi User Admins as well as Client Users.
 */
@Component
public class LegacyAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Resource
    UserAdminPersistence userAdminPersistence;

    @Resource
    UserClientPersistence userClientPersistence;

    @Resource
    UserDetailsService userDetailsService;

    @Resource
    PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return super.authenticate(authentication);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
        // make sure password matches the hashed password
        String presentedPassword = authentication.getCredentials().toString();
        String password = userDetails.getPassword();
        if (userDetails instanceof UserClient) {
            password += ((UserClient) userDetails).getSalt();
        }
        if (userDetails instanceof UserAdmin) {
            password += ((UserAdmin) userDetails).getSalt();
        }
        if (!passwordEncoder.matches(presentedPassword, password)) {
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
        if (userDetails instanceof UserClient) {
            // mark that the user has logged in successfully
            UserClient userClient = ((UserClient) userDetails);
            userClient.setPasswordResetExpirationDateTime(null);
            userClient.setPasswordResetToken(null);
            if (userClient.isNeverLoggedIn()) {
                userClient.setNeverLoggedIn(false);
                userClient.setActivationKey(null);
                userClient.setActivationExpirationDateTime(null);
                userClientPersistence.saveOrUpdate(userClient);
            }
        }
    }

    /**
     * Delegates to user details
     *
     * @param username       to fetch
     * @param authentication a password
     * @return a UserDetails object that is made up of the UserAdmin or UserClient
     * @throws AuthenticationException if the user is not found
     */
    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        return userDetailsService.loadUserByUsername(username);
    }

}
