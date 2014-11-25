package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.user.client.UserClientRole;
import com.emmisolutions.emmimanager.persistence.UserClientRolePersistence;
import com.emmisolutions.emmimanager.persistence.repo.UserClientRoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Spring data implementation class
 */
@Repository
public class UserClientRolePersistenceImpl implements UserClientRolePersistence {

    @Resource
    UserClientRoleRepository userClientRoleRepository;

    @Override
    public Page<UserClientRole> find(long clientId, Pageable page) {
        return userClientRoleRepository.findByClientId(clientId, page);
    }

    @Override
    public UserClientRole save(UserClientRole userClientRole) {
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
}
