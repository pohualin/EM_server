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
    UserClientRepository userClientRepository;

    @Override
    public UserClient saveOrUpdate(UserClient user) {
	return userClientRepository.save(user);
    }

    @Override
    public UserClient reload(Long userClientId) {
	if (userClientId == null) {
	    return null;
	}
	return userClientRepository.findOne(userClientId);
    }

}
