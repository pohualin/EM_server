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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
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
			// In case of an UserClient was locked and lock is expired.
			// We want to initiate another transaction to unlock the UserClient and
			// reset failure count.
			if (userDetails instanceof UserClient) {
				UserClient toUpdate = (UserClient) userDetails;
				if (toUpdate.getLockExpirationDateTime() != null
						&& LocalDateTime.now(DateTimeZone.UTC).isAfter(
								toUpdate.getLockExpirationDateTime())) {
					userClientService
							.resetUserClientLock((UserClient) userDetails);
				}
			}
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
        if (userDetails instanceof UserClient) {
            // mark that the user has logged in successfully
            UserClient userClient = ((UserClient) userDetails);
            UserClient unlockedUser = userClientPersistence.unlockUserClient(userClient);
            unlockedUser.setPasswordResetExpirationDateTime(null);
            unlockedUser.setPasswordResetToken(null);
            if (unlockedUser.isNeverLoggedIn()) {
                unlockedUser.setNeverLoggedIn(false);
                unlockedUser.setActivationKey(null);
                unlockedUser.setActivationExpirationDateTime(null);
            }
            
            // Set credential to expire if password expired
            if (unlockedUser.getPasswordExpireationDateTime() != null
                    && LocalDateTime.now(DateTimeZone.UTC).isAfter(
                            unlockedUser.getPasswordExpireationDateTime())) {
                unlockedUser.setCredentialsNonExpired(false);
            }
            
            userClientPersistence.saveOrUpdate(unlockedUser);
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
        UserDetails user = userDetailsService.loadUserByUsername(username);
        if (user instanceof UserClient) {
            UserClient toUpdate = (UserClient) user;
            // User is locked but the lock is expired
            if (!toUpdate.isAccountNonLocked()
    				&& toUpdate.getLockExpirationDateTime() != null
    				&& LocalDateTime.now(DateTimeZone.UTC).isAfter(
    						toUpdate.getLockExpirationDateTime())) {
                // By pass PreAuthentication check by setting this flag to true
    			toUpdate.setAccountNonLocked(true);
    		}
        }
        return user;
    }

    @Override
    public void setUserDetailsService(UserDetailsService userDetailsService){
        this.userDetailsService = userDetailsService;
    }
    
}
