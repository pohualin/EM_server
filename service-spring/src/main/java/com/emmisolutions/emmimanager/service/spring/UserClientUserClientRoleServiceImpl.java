package com.emmisolutions.emmimanager.service.spring;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.UserClientUserClientRole;
import com.emmisolutions.emmimanager.persistence.UserClientUserClientRolePersistence;
import com.emmisolutions.emmimanager.service.UserClientService;
import com.emmisolutions.emmimanager.service.UserClientUserClientRoleService;

/**
 * 
 * 
 */
@Service
public class UserClientUserClientRoleServiceImpl implements
	UserClientUserClientRoleService {

    @Resource
    UserClientService userClientService;

    @Resource
    UserClientUserClientRolePersistence userClientUserClientRolePersistence;

    @Override
    public UserClientUserClientRole create(
	    UserClientUserClientRole userClientUserClientRole) {
	return userClientUserClientRolePersistence
		.saveOrUpdate(userClientUserClientRole);
    }

    @Override
    public Page<UserClientUserClientRole> findByUserClient(Long userClientId,
	    Pageable pageable) {
	UserClient userClient = userClientService.reload(userClientId);
	return userClientUserClientRolePersistence.findByUserClient(userClient,
		pageable);
    }

}
