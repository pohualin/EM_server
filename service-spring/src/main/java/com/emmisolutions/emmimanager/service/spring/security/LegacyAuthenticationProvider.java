package com.emmisolutions.emmimanager.service.spring.security;

import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminPermission;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminUserAdminRole;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.UserClientPermission;
import com.emmisolutions.emmimanager.model.user.client.UserClientUserClientRole;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamPermission;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Replacement for the DaoAuthentication provider to support legacy emmi passwords.
 * This class supports Emmi User Admins as well as Client Users.
 */
@Component
public class LegacyAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Resource
    UserPersistence userPersistence;

    @Resource
    UserClientPersistence userClientPersistence;

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
        if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
    }

    /**
     * In legacy Emmi, we put the salt value in a different database column rather than within the
     * password itself. This method bridges the older way (separate columns) to the newer way by
     * setting the 'password' to a concatenation of the salt and the password itself.
     *
     * @param username       to fetch
     * @param authentication a password
     * @return a UserDetails object that is made up of the UserAdmin or UserClient
     * @throws AuthenticationException if the user is not found
     */
    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        UserAdmin userAdmin = userPersistence.reload(username);
        if (userAdmin != null) {
            Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            for (UserAdminUserAdminRole userAdminUserAdminRole : userAdmin.getRoles()) {
                for (UserAdminPermission permission : userAdminUserAdminRole.getUserAdminRole().getPermissions()) {
                    grantedAuthorities.add(new SimpleGrantedAuthority(permission.getName().toString()));
                }
            }
            return new org.springframework.security.core.userdetails.User(userAdmin.getLogin(), getPassword(userAdmin),
                    grantedAuthorities);
        } else {
            UserClient userClient = userClientPersistence.fetchUserWillFullPermissions(username);
            if (userClient == null) {
                throw new UsernameNotFoundException("User " + username + " was not found.");
            }
            Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            for (UserClientUserClientRole clientRole : userClient.getClientRoles()) {
                for (UserClientPermission permission : clientRole.getUserClientRole().getUserClientPermissions()) {
                    grantedAuthorities.add(new SimpleGrantedAuthority(permission.getName().toString()));
                }
            }
            // granted team authorities are in the form of PERM_NAMEOFPERMISSION_TEAMID
            for (UserClientUserClientTeamRole teamRole : userClient.getTeamRoles()) {
                for (UserClientTeamPermission permission : teamRole.getUserClientTeamRole().getUserClientTeamPermissions()) {
                    grantedAuthorities.add(new SimpleGrantedAuthority(permission.getName().toString() + "_" + teamRole.getTeam().getId()));
                }
            }
            return new org.springframework.security.core.userdetails.User(userClient.getLogin(), getPassword(userClient),
                    grantedAuthorities);
        }
    }

    /**
     * Create a full password from the parts of the User object
     *
     * @param user to create a password
     * @return a string which concatenates password|salt
     */
    private String getPassword(User user) {
        // set the password to be the concatenation of password HEX and salt HEX
        return user.getPassword() + user.getSalt();
    }
}
