package com.emmisolutions.emmimanager.service.spring.security;


import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.service.security.UserDetailsService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;

/**
 * The UserDetailsService implementation used by client facing applications.
 */
@Service("clientUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserClientPersistence userClientPersistence;

    @Override
    @Transactional(readOnly = true)
    public User loadUserByUsername(final String login) {
        User ret = userClientPersistence.fetchUserWillFullPermissions(login);
        if (ret == null || CollectionUtils.isEmpty(ret.getAuthorities())){
            throw new UsernameNotFoundException("User " + login + " was not found.");
        }
        return ret;
    }

    @Override
    @Transactional(readOnly = true)
    public User getLoggedInUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        User user = null;
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof User) {
                user = (User) authentication.getPrincipal();
            } else if (authentication.getPrincipal() instanceof String) {
                user = loadUserByUsername((String) authentication.getPrincipal());
            }
        }
        return user;
    }
}
