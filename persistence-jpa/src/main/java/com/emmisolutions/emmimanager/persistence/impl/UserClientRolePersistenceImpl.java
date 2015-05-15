package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.user.client.UserClientPermission;
import com.emmisolutions.emmimanager.model.user.client.UserClientRole;
import com.emmisolutions.emmimanager.persistence.UserClientReferenceRolePersistence;
import com.emmisolutions.emmimanager.persistence.UserClientRolePersistence;
import com.emmisolutions.emmimanager.persistence.impl.specification.MatchingCriteriaBean;
import com.emmisolutions.emmimanager.persistence.repo.UserClientPermissionRepository;
import com.emmisolutions.emmimanager.persistence.repo.UserClientRoleRepository;
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
public class UserClientRolePersistenceImpl implements UserClientRolePersistence {

    @Resource
    UserClientRoleRepository userClientRoleRepository;

    @Resource
    UserClientReferenceRolePersistence userClientReferenceRolePersistence;

    @Resource
    UserClientPermissionRepository userClientPermissionRepository;

    @Resource
    MatchingCriteriaBean matchCriteria;

    @Override
    public Page<UserClientRole> find(long clientId, Pageable page) {
        return userClientRoleRepository.findByClientId(clientId, page);
    }

    @Caching(evict = {
            @CacheEvict(value = "clientFindById", allEntries = true),
            @CacheEvict(value = "clientFindByLoginIgnoreCase", allEntries = true)
    })
    @Override
    public UserClientRole save(UserClientRole userClientRole) {
        if (userClientRole == null) {
            throw new InvalidDataAccessApiUsageException(
                    "UserClientRole cannot be null");
        }
        userClientRole.setNormalizedName(matchCriteria
                .normalizedName(userClientRole.getName()));
        // reload the type because the version may have changed
        userClientRole.setType(userClientReferenceRolePersistence
                .reload(userClientRole.getType()));
        return userClientRoleRepository.save(userClientRole);
    }

    @Override
    public UserClientRole reload(UserClientRole userClientRole) {
        if (userClientRole == null || userClientRole.getId() == null) {
            return null;
        }
        return userClientRoleRepository.findOne(userClientRole.getId());
    }

    @Caching(evict = {
            @CacheEvict(value = "clientFindById", allEntries = true),
            @CacheEvict(value = "clientFindByLoginIgnoreCase", allEntries = true)
    })
    @Override
    public void remove(Long id) {
        userClientRoleRepository.delete(id);
    }

    @Override
    public Set<UserClientPermission> loadPossiblePermissions() {
        return new HashSet<>(userClientPermissionRepository.findAll());
    }

    @Override
    public Set<UserClientPermission> permissionsFor(
            UserClientRole userClientRole) {
        if (userClientRole == null || userClientRole.getId() == null) {
            return new HashSet<>();
        }
        return userClientPermissionRepository
                .findAllByUserClientRolesId(userClientRole.getId());
    }

    @Override
    public UserClientRole findDuplicateByName(UserClientRole userClientRole) {
        UserClientRole duplicate = null;
        if (userClientRole != null) {
            String toSearch = matchCriteria
                    .normalizedName(userClientRole.getName());
            if (StringUtils.isNotBlank(toSearch)) {
                UserClientRole sameNameAndClientInDb = userClientRoleRepository
                        .findByNormalizedNameAndClient(toSearch,
                                userClientRole.getClient());
                if (userClientRole.equals(sameNameAndClientInDb)) {
                    duplicate = sameNameAndClientInDb;
                }
            }
        }
        return duplicate;
    }

}
