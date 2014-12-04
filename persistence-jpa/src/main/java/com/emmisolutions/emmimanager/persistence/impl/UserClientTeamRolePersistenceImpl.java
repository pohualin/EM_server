package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamPermission;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamRole;
import com.emmisolutions.emmimanager.persistence.UserClientReferenceTeamRolePersistence;
import com.emmisolutions.emmimanager.persistence.UserClientTeamRolePersistence;
import com.emmisolutions.emmimanager.persistence.repo.UserClientTeamPermissionRepository;
import com.emmisolutions.emmimanager.persistence.repo.UserClientTeamRoleRepository;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * Spring data implementation class
 */
@Repository
public class UserClientTeamRolePersistenceImpl implements UserClientTeamRolePersistence {

    @Resource
    UserClientTeamRoleRepository userClientTeamRoleRepository;

    @Resource
    UserClientTeamPermissionRepository userClientTeamPermissionRepository;

    @Resource
    UserClientReferenceTeamRolePersistence userClientReferenceTeamRolePersistence;

    @Override
    public Page<UserClientTeamRole> find(long clientId, Pageable page) {
        return userClientTeamRoleRepository.findByClientId(clientId, page);
    }

    @Override
    public UserClientTeamRole save(UserClientTeamRole userClientTeamRole) {
        if (userClientTeamRole == null){
            throw new InvalidDataAccessApiUsageException("UserClientTeamRole cannot be null");
        }
        // reload the type because the version may have changed
        userClientTeamRole.setType(userClientReferenceTeamRolePersistence.reload(userClientTeamRole.getType()));
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

    @Override
    public Set<UserClientTeamPermission> loadPossiblePermissions() {
        return new HashSet<>(userClientTeamPermissionRepository.findAll());
    }

}
