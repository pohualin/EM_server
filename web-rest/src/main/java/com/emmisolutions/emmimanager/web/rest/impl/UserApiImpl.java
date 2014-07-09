package com.emmisolutions.emmimanager.web.rest.impl;

import com.emmisolutions.emmimanager.api.UserApi;
import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Implementation of UserApi
 */
@Component
public class UserApiImpl implements UserApi {

    @Resource
    UserService userService;

    @Override
    public User getUser(User user) {
        return user;
    }

    @Override
    public User create(User user) {
        // no-op
        return new User();
    }

    @Override
    public User save(User user) {
        return userService.save(user);
    }

    @Override
    public void delete(User user) {
        // no-op
    }

    @Override
    public User loggedIn() {
        return userService.loggedIn();
    }
}
