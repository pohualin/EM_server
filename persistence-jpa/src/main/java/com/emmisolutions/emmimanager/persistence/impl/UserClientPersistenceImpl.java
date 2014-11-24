package com.emmisolutions.emmimanager.persistence.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.persistence.repo.UserClientRepository;

/**
 * Repo to deal with user persistence.
 */
@Repository
public class UserClientPersistenceImpl implements UserClientPersistence {

    @Resource
    UserClientRepository userAdminRepository;

    @Override
    public UserClient saveOrUpdate(UserClient user) {
        return userAdminRepository.save(user);
    }

}
