package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamRole;
import com.emmisolutions.emmimanager.persistence.UserClientTeamRolePersistence;
import com.emmisolutions.emmimanager.persistence.repo.UserClientTeamRoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Spring data implementation class
 */
@Repository
public class UserClientTeamRolePersistenceImpl implements UserClientTeamRolePersistence {

    @Resource
    UserClientTeamRoleRepository userClientTeamRoleRepository;

    @Override
    public Page<UserClientTeamRole> find(long clientId, Pageable page) {
        return userClientTeamRoleRepository.findByClientId(clientId, page);
    }

    @Override
    public UserClientTeamRole save(UserClientTeamRole userClientTeamRole) {
        return userClientTeamRoleRepository.save(userClientTeamRole);
    }

    @Override
    public UserClientTeamRole reload(UserClientTeamRole userClientTeamRole) {
        if (userClientTeamRole == null || userClientTeamRole.getId() == null) {
            return null;
        }
        return userClientTeamRoleRepository.findOne(userClientTeamRole.getId());
    }

    @Override
    public void remove(Long id) {
        userClientTeamRoleRepository.delete(id);
    }
}
