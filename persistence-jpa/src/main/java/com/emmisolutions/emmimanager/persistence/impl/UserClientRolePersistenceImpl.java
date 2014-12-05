package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.user.client.UserClientPermission;
import com.emmisolutions.emmimanager.model.user.client.UserClientRole;
import com.emmisolutions.emmimanager.persistence.UserClientReferenceRolePersistence;
import com.emmisolutions.emmimanager.persistence.UserClientRolePersistence;
import com.emmisolutions.emmimanager.persistence.repo.UserClientPermissionRepository;
import com.emmisolutions.emmimanager.persistence.repo.UserClientRoleRepository;
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
public class UserClientRolePersistenceImpl implements UserClientRolePersistence {

    @Resource
    UserClientRoleRepository userClientRoleRepository;

    @Resource
    UserClientReferenceRolePersistence userClientReferenceRolePersistence;

    @Resource
    UserClientPermissionRepository userClientPermissionRepository;

    @Override
    public Page<UserClientRole> find(long clientId, Pageable page) {
        return userClientRoleRepository.findByClientId(clientId, page);
    }

    @Override
    public UserClientRole save(UserClientRole userClientRole) {
        if (userClientRole == null){
            throw new InvalidDataAccessApiUsageException("UserClientRole cannot be null");
        }
        // reload the type because the version may have changed
        userClientRole.setType(userClientReferenceRolePersistence.reload(userClientRole.getType()));
        return userClientRoleRepository.save(userClientRole);
    }

    @Override
    public UserClientRole reload(UserClientRole userClientRole) {
        if (userClientRole == null || userClientRole.getId() == null) {
            return null;
        }
        return userClientRoleRepository.findOne(userClientRole.getId());
    }

    @Override
    public void remove(Long id) {
        userClientRoleRepository.delete(id);
    }

    @Override
    public Set<UserClientPermission> loadPossiblePermissions() {
        return new HashSet<>(userClientPermissionRepository.findAll());
    }

    @Override
    public Set<UserClientPermission> permissionsFor(UserClientRole userClientRole) {
        if (userClientRole == null || userClientRole.getId() == null) {
            return null;
        }
        return userClientPermissionRepository.findAllByUserClientRolesId(userClientRole.getId());
    }

}
