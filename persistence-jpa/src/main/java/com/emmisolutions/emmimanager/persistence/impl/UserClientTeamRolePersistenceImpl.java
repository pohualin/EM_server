package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamPermission;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamRole;
import com.emmisolutions.emmimanager.persistence.UserClientReferenceTeamRolePersistence;
import com.emmisolutions.emmimanager.persistence.UserClientTeamRolePersistence;
import com.emmisolutions.emmimanager.persistence.impl.specification.MatchingCriteriaBean;
import com.emmisolutions.emmimanager.persistence.repo.UserClientTeamPermissionRepository;
import com.emmisolutions.emmimanager.persistence.repo.UserClientTeamRoleRepository;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
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
    
    @Resource
    MatchingCriteriaBean matchCriteria;

    @Override
    public Page<UserClientTeamRole> find(long clientId, Pageable page) {
        return userClientTeamRoleRepository.findByClientId(clientId, page);
    }

    @Caching(evict = {
            @CacheEvict(value = "clientFindById", allEntries = true),
            @CacheEvict(value = "clientFindByLoginIgnoreCase", allEntries = true)
    })
    @Override
    public UserClientTeamRole save(UserClientTeamRole userClientTeamRole) {
        if (userClientTeamRole == null){
            throw new InvalidDataAccessApiUsageException("UserClientTeamRole cannot be null");
        }
        userClientTeamRole.setNormalizedName(matchCriteria
                .normalizedName(userClientTeamRole.getName()));
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

    @Caching(evict = {
            @CacheEvict(value = "clientFindById", allEntries = true),
            @CacheEvict(value = "clientFindByLoginIgnoreCase", allEntries = true)
    })
    @Override
    public void remove(Long id) {
        userClientTeamRoleRepository.delete(id);
    }

    @Override
    public Set<UserClientTeamPermission> loadPossiblePermissions() {
        return new HashSet<>(userClientTeamPermissionRepository.findAll());
    }

    @Override
    public Set<UserClientTeamPermission> permissionsFor(UserClientTeamRole userClientRole) {
        if (userClientRole == null || userClientRole.getId() == null) {
            return new HashSet<>();
        }
        return userClientTeamPermissionRepository.findAllByUserClientTeamRolesId(userClientRole.getId());
    }
    
    @Override
    public UserClientTeamRole findByNormalizedName(UserClientTeamRole userClientTeamRole) {
        String toSearch = matchCriteria
                .normalizedName(userClientTeamRole.getName());
        if (StringUtils.isNotBlank(toSearch)) {
            return userClientTeamRoleRepository.findByNormalizedNameAndClient(
                    toSearch, userClientTeamRole.getClient());
        } else {
            return null;
        }
    }

}
