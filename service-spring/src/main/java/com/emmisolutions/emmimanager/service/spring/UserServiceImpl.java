package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.service.UserService;
import com.emmisolutions.emmimanager.service.spring.security.SecurityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

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

    @Override
    @Transactional
    public UserAdmin save(UserAdmin user) {
        return userPersistence.saveOrUpdate(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserAdmin loggedIn() {
        return userPersistence.fetchUserWillFullPermissions(SecurityUtils.getCurrentLogin());
    }
}
