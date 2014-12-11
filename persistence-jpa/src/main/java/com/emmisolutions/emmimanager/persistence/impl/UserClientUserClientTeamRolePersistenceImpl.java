package com.emmisolutions.emmimanager.persistence.impl;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;
import com.emmisolutions.emmimanager.persistence.UserClientUserClientTeamRolePersistence;
import com.emmisolutions.emmimanager.persistence.repo.UserClientUserClientTeamRoleRepository;

/**
 * Spring data implementation class
 */
@Repository
public class UserClientUserClientTeamRolePersistenceImpl implements
	UserClientUserClientTeamRolePersistence {

    @Resource
    UserClientUserClientTeamRoleRepository userClientUserClientTeamRoleRepository;

    @Override
    public UserClientUserClientTeamRole saveOrUpdate(
	    UserClientUserClientTeamRole userClientUserClientTeamRole) {
	return userClientUserClientTeamRoleRepository
		.save(userClientUserClientTeamRole);
    }

    @Override
    public Page<UserClientUserClientTeamRole> findByUserClient(
	    UserClient userClient, Pageable pageable) {
	if (pageable == null) {
	    pageable = new PageRequest(0, 10, Sort.Direction.ASC, "id");
	}
	return userClientUserClientTeamRoleRepository.findByUserClient(userClient,
		pageable);
    }

    @Override
    public UserClientUserClientTeamRole reload(Long userClientUserClientId) {
	if (userClientUserClientId == null) {
	    return null;
	}
	return userClientUserClientTeamRoleRepository
		.findOne(userClientUserClientId);
    }

    @Override
    public void delete(Long userClientUserClientId) {
	userClientUserClientTeamRoleRepository.delete(userClientUserClientId);
    }

}
