package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;
import com.emmisolutions.emmimanager.persistence.UserClientUserClientTeamRolePersistence;
import com.emmisolutions.emmimanager.persistence.repo.UserClientUserClientTeamRoleRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

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

    @Caching(evict = {
            @CacheEvict(value = "clientFindByLoginIgnoreCase", allEntries = true)
    })
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
        Pageable pageableToUse;
        if (pageable == null) {
            pageableToUse = new PageRequest(0, 10, Sort.Direction.ASC, "id");
        } else {
            pageableToUse = pageable;
        }
        return userClientUserClientTeamRoleRepository
                .findByUserClientIdAndUserClientTeamRoleId(userClientId,
                        userClientTeamRoleId, pageableToUse);
    }

    @Override
    public UserClientUserClientTeamRole reload(Long userClientUserClientId) {
        return userClientUserClientTeamRoleRepository
                .findOne(userClientUserClientId);
    }

    @Caching(evict = {
            @CacheEvict(value = "clientFindByLoginIgnoreCase", allEntries = true)
    })
    @Override
    public UserClientUserClientTeamRole saveOrUpdate(
            UserClientUserClientTeamRole userClientUserClientTeamRole) {
        return userClientUserClientTeamRoleRepository
                .save(userClientUserClientTeamRole);
    }
}
