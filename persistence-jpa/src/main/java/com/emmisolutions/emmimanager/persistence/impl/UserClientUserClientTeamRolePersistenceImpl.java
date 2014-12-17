package com.emmisolutions.emmimanager.persistence.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.Team;
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
    public void delete(Long userClientUserClientId) {
	userClientUserClientTeamRoleRepository.delete(userClientUserClientId);
    }

    @Override
    public void delete(Long userClientId, Long userClientTeamRoleId) {
	userClientUserClientTeamRoleRepository
		.deleteAllByUserClientIdAndUserClientTeamRoleId(userClientId,
			userClientTeamRoleId);
    }

    @Override
    public List<UserClientUserClientTeamRole> findByUserClientIdAndTeamsIn(
	    Long userClientId, List<Team> teams) {
	return userClientUserClientTeamRoleRepository
		.findByUserClientIdAndTeamIn(userClientId, teams);
    }

    @Override
    public Page<UserClientUserClientTeamRole> findByUserClientIdAndUserClientTeamRoleId(
	    Long userClientId, Long userClientTeamRoleId, Pageable pageable) {
	if (pageable == null) {
	    pageable = new PageRequest(0, 10, Sort.Direction.ASC, "id");
	}
	return userClientUserClientTeamRoleRepository
		.findByUserClientIdAndUserClientTeamRoleId(userClientId,
			userClientTeamRoleId, pageable);
    }

    @Override
    public UserClientUserClientTeamRole reload(Long userClientUserClientId) {
	return userClientUserClientTeamRoleRepository
		.findOne(userClientUserClientId);
    }

    @Override
    public UserClientUserClientTeamRole saveOrUpdate(
	    UserClientUserClientTeamRole userClientUserClientTeamRole) {
	return userClientUserClientTeamRoleRepository
		.save(userClientUserClientTeamRole);
    }
}
