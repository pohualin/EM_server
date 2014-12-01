package com.emmisolutions.emmimanager.service.spring;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.service.UserClientService;

/**
 * An example of a Service class. It can only contact the persistence layer
 * and is responsible for Transaction demarcation. This layer will also have
 * security annotations at the method level as well.
 */
@Service
public class UserClientServiceImpl implements UserClientService {

    @Resource
    UserClientPersistence userClientPersistence;

    @Override
    @Transactional
    public UserClient save(UserClient user) {
    	if(StringUtils.isBlank(user.getLogin()) && StringUtils.isNotBlank(user.getEmail())){
    		user.setLogin(user.getEmail());
    	}
        return userClientPersistence.saveOrUpdate(user);
    }
}
