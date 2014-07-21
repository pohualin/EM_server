package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.persistence.repo.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class UserPersistenceImpl implements UserPersistence {

    @Resource
    UserRepository userRepository;

    @Override
    public User saveOrUpdate(User user) {
        user.setLogin(StringUtils.lowerCase(user.getLogin()));
        return userRepository.save(user);
    }

    @Override
    public User reload(String login) {
        return userRepository.findByLoginIgnoreCase(login);
    }

    @Override
    public User fetchUserWillFullPermissions(String login) {
        return userRepository.fetchWithFullPermissions(login);
    }
}
