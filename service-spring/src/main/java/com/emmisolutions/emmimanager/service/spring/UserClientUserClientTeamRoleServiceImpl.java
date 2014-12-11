package com.emmisolutions.emmimanager.service.spring;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;
import com.emmisolutions.emmimanager.persistence.UserClientUserClientTeamRolePersistence;
import com.emmisolutions.emmimanager.service.UserClientService;
import com.emmisolutions.emmimanager.service.UserClientTeamRoleService;
import com.emmisolutions.emmimanager.service.UserClientUserClientTeamRoleService;

/**
 * 
 * 
 */
@Service
public class UserClientUserClientTeamRoleServiceImpl implements
	UserClientUserClientTeamRoleService {

    @Resource
    UserClientService userClientService;

    @Resource
    UserClientTeamRoleService userClientTeamRoleService;

    @Resource
    UserClientUserClientTeamRolePersistence userClientUserClientTeamRolePersistence;

    @Override
    public UserClientUserClientTeamRole create(
	    UserClientUserClientTeamRole userClientUserClientTeamRole) {
	userClientUserClientTeamRole.setUserClient(userClientService
		.reload(userClientUserClientTeamRole.getUserClient().getId()));
	userClientUserClientTeamRole
		.setUserClientTeamRole(userClientTeamRoleService
			.reload(userClientUserClientTeamRole
				.getUserClientTeamRole()));
	return userClientUserClientTeamRolePersistence
		.saveOrUpdate(userClientUserClientTeamRole);
    }

    @Override
    public Page<UserClientUserClientTeamRole> findByUserClient(
	    Long userClientId, Pageable pageable) {
	UserClient userClient = userClientService.reload(userClientId);
	return userClientUserClientTeamRolePersistence.findByUserClient(
		userClient, pageable);
    }

    @Override
    public UserClientUserClientTeamRole reload(Long userClientUserClientId) {
	return userClientUserClientTeamRolePersistence
		.reload(userClientUserClientId);
    }

    @Override
    public void delete(Long userClientUserClientId) {
	userClientUserClientTeamRolePersistence.delete(userClientUserClientId);
    }

}
