package com.emmisolutions.emmimanager.service.spring;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.service.UserClientService;

/**
 * It can only contact the persistence layer and is responsible for Transaction
 * demarcation. This layer will also have security annotations at the method
 * level as well.
 */
@Service
public class UserClientServiceImpl implements UserClientService {

    @Resource
    UserClientPersistence userClientPersistence;

    @Override
    @Transactional
    public UserClient create(UserClient userClient) {
	return userClientPersistence.saveOrUpdate(userClient);
    }

    @Override
    @Transactional
    public UserClient reload(Long userClientId) {
	return userClientPersistence.reload(userClientId);
    }

    @Override
    @Transactional
    public UserClient update(UserClient user) {
	// TODO Auto-generated method stub
	return null;
    }
}
