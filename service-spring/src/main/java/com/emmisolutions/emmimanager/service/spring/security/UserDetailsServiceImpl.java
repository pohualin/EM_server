package com.emmisolutions.emmimanager.service.spring.security;


import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminPermission;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminUserAdminRole;
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

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String login) {
        UserAdmin userFromDatabase = userPersistence.reload(login);
        if (userFromDatabase == null) {
            throw new UsernameNotFoundException("User " + login + " was not found in the database");
        }
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (UserAdminUserAdminRole userAdminUserAdminRole : userFromDatabase.getRoles()) {
            for (UserAdminPermission permission : userAdminUserAdminRole.getUserAdminRole().getPermissions()) {
                grantedAuthorities.add(new SimpleGrantedAuthority(permission.getName().toString()));
            }
        }
        return new org.springframework.security.core.userdetails.User(userFromDatabase.getLogin(), userFromDatabase.getPassword(),
                grantedAuthorities);
    }
}
