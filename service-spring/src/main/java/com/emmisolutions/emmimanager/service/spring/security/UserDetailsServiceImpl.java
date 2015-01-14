package com.emmisolutions.emmimanager.service.spring.security;


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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;

/**
 * The UserDetailsService implementation used by Spring Security.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserPersistence userPersistence;

    @Resource
    private UserClientPersistence userClientPersistence;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String login) {
        UserAdmin userAdmin = userPersistence.reload(login);
        UserClient userClient = userClientPersistence.fetchUserWillFullPermissions(login);
        if (userAdmin == null && userClient == null) {
            throw new UsernameNotFoundException("User " + login + " was not found in the database");
        }
        if (userAdmin != null) {
            Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            for (UserAdminUserAdminRole userAdminUserAdminRole : userAdmin.getRoles()) {
                for (UserAdminPermission permission : userAdminUserAdminRole.getUserAdminRole().getPermissions()) {
                    grantedAuthorities.add(new SimpleGrantedAuthority(permission.getName().toString()));
                }
            }
            return new org.springframework.security.core.userdetails.User(userAdmin.getLogin(), userAdmin.getPassword(),
                    grantedAuthorities);
        } else {
            Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            for (UserClientUserClientRole clientRole : userClient.getClientRoles()) {
                for (UserClientPermission permission : clientRole.getUserClientRole().getUserClientPermissions()) {
                    grantedAuthorities.add(new SimpleGrantedAuthority(permission.getName().toString()));
                }
            }
            for (UserClientUserClientTeamRole teamRole : userClient.getTeamRoles()) {
                for (UserClientTeamPermission permission : teamRole.getUserClientTeamRole().getUserClientTeamPermissions()) {
                    grantedAuthorities.add(new SimpleGrantedAuthority(permission.getName().toString()));
                }
            }
            return new org.springframework.security.core.userdetails.User(userClient.getLogin(), userClient.getPassword(),
                    grantedAuthorities);
        }
    }
}
