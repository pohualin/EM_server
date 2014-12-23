package com.emmisolutions.emmimanager.service.spring;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emmisolutions.emmimanager.model.UserSearchFilter;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.service.UserService;
import com.emmisolutions.emmimanager.service.spring.security.SecurityUtils;

/**
 * An example of a Service class. It can only contact the persistence layer
 * and is responsible for Transaction demarcation. This layer will also have
 * security annotations at the method level as well.
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    PasswordEncoder passwordEncoder;

    @Resource
    UserPersistence userPersistence;

    @Resource
    SecurityUtils securityUtils;

    @Override
    @Transactional
    public UserAdmin save(UserAdmin user) {
        return userPersistence.saveOrUpdate(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserAdmin loggedIn() {
        return userPersistence.fetchUserWillFullPermissions(securityUtils.getCurrentLogin());
    }

    @Override
    @Transactional
    public UserAdmin reload(UserAdmin user) {
        if (user == null || user.getId() == null) {
            return null;
        }
        return userPersistence.reload(user);
    }
 
    /**
     * @param page             Page number of users needed
     * @param userSearchFilter search filter for teams
     * @return a particular page of users
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserAdmin> list(Pageable page, UserSearchFilter userSearchFilter) {
        return userPersistence.list(page, userSearchFilter);
    }        
}
