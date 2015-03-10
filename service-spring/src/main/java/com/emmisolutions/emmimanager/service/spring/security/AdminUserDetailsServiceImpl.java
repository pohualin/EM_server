package com.emmisolutions.emmimanager.service.spring.security;

import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.persistence.UserAdminPersistence;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;

/**
 * UserDetails service for administrators only
 */
@Service("adminUserDetailsService")
public class AdminUserDetailsServiceImpl extends UserDetailsServiceImpl {

    @Resource
    private transient UserAdminPersistence userPersistence;

    @Override
    @Transactional(readOnly = true)
    public User loadUserByUsername(final String login) {
        UserAdmin ret = userPersistence.fetchUserWillFullPermissions(login);
        if (ret == null || CollectionUtils.isEmpty(ret.getAuthorities())){
            throw new UsernameNotFoundException("User " + login + " was not found.");
        }
        if (StringUtils.isBlank(ret.getPassword())) {
            // need to set this so that remember me cookies will work
            ret.setPassword("*********admin********");
        }
        return ret;
    }
}
