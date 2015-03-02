package com.emmisolutions.emmimanager.service.spring.security;

import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.persistence.UserAdminPersistence;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.service.UserClientService;
import com.emmisolutions.emmimanager.service.security.UserDetailsConfigurableAuthenticationProvider;
import com.emmisolutions.emmimanager.service.security.UserDetailsService;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Replacement for the DaoAuthentication provider to support legacy emmi passwords.
 * This class supports Emmi User Admins as well as Client Users.
 */
@Component
@Scope(value = "prototype")
public class LegacyAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider
        implements UserDetailsConfigurableAuthenticationProvider {

    @Resource
    UserClientService userClientService;
    
    @Resource
    UserAdminPersistence userAdminPersistence;

    @Resource
    UserClientPersistence userClientPersistence;

    @Resource
    PasswordEncoder passwordEncoder;

    private UserDetailsService userDetailsService;

    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        super.setPreAuthenticationChecks(new LegacyAuthenticationProvider.PreAuthenticationChecks());
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
            if(!userClient.isAccountNonLocked() || userClient.getLoginFailureCount() != 0 || userClient.getLockExpirationDateTime() != null){
                userClient.setLoginFailureCount(0);
                userClient.setLockExpirationDateTime(null);
                userClient.setAccountNonLocked(true);
                userClientPersistence.saveOrUpdate(userClient);
            }
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

    @Override
    public void setUserDetailsService(UserDetailsService userDetailsService){
        this.userDetailsService = userDetailsService;
    }
    
    private class PreAuthenticationChecks implements UserDetailsChecker {
        public void check(UserDetails user) {
            if (!user.isAccountNonLocked()) {
                if (user instanceof UserClient) {
                    UserClient toUpdate = (UserClient) user;
                    if (toUpdate.isAccountNonLocked() == false
                            && toUpdate.getLockExpirationDateTime() != null
                            && LocalDateTime.now(DateTimeZone.UTC).isAfter(
                                    toUpdate.getLockExpirationDateTime())) {
                        // Do not issue LockException
                    } else {
                        throw new LockedException(
                                messages.getMessage(
                                        "AbstractUserDetailsAuthenticationProvider.locked",
                                        "User account is locked"), user);
                    }
                } else {
                    throw new LockedException(messages.getMessage(
                            "AbstractUserDetailsAuthenticationProvider.locked",
                            "User account is locked"), user);
                }
            }

            if (!user.isEnabled()) {
                throw new DisabledException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.disabled",
                        "User is disabled"), user);
            }

            if (!user.isAccountNonExpired()) {
                throw new AccountExpiredException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.expired",
                        "User account has expired"), user);
            }
        }
    }

}
