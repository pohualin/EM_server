package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.UserClientUserClientRole;
import com.emmisolutions.emmimanager.persistence.UserClientUserClientRolePersistence;
import com.emmisolutions.emmimanager.persistence.repo.UserClientUserClientRoleRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Spring data implementation class
 */
@Repository
public class UserClientUserClientRolePersistenceImpl implements
        UserClientUserClientRolePersistence {

    @Resource
    UserClientUserClientRoleRepository userClientUserClientRoleRepository;

    @Caching(evict = {
            @CacheEvict(value = "clientFindById", allEntries = true),
            @CacheEvict(value = "clientFindByLoginIgnoreCase", allEntries = true)
    })
    @Override
    public UserClientUserClientRole saveOrUpdate(
            UserClientUserClientRole userClientUserClientRole) {
        return userClientUserClientRoleRepository
                .save(userClientUserClientRole);
    }

    @Override
    public Page<UserClientUserClientRole> findByUserClient(
            UserClient userClient, Pageable pageable) {
        if (pageable == null) {
            pageable = new PageRequest(0, 10, Sort.Direction.ASC, "id");
        }
        return userClientUserClientRoleRepository.findByUserClient(userClient,
                pageable);
    }

    @Override
    public UserClientUserClientRole reload(Long userClientUserClientId) {
        if (userClientUserClientId == null) {
            return null;
        }
        return userClientUserClientRoleRepository
                .findOne(userClientUserClientId);
    }

    @Caching(evict = {
            @CacheEvict(value = "clientFindById", allEntries = true),
            @CacheEvict(value = "clientFindByLoginIgnoreCase", allEntries = true)
    })
    @Override
    public void delete(Long userClientUserClientId) {
        userClientUserClientRoleRepository.delete(userClientUserClientId);
    }

}
