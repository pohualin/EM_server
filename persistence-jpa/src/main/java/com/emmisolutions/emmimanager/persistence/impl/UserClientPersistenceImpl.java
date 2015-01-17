package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.UserClientSearchFilter;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.persistence.impl.specification.MatchingCriteriaBean;
import com.emmisolutions.emmimanager.persistence.impl.specification.UserClientSpecifications;
import com.emmisolutions.emmimanager.persistence.repo.UserClientRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * Repo to deal with user persistence.
 */
@Repository
public class UserClientPersistenceImpl implements UserClientPersistence {

    @Resource
    UserClientRepository userClientRepository;

    @Resource
    UserClientSpecifications userClientSpecifications;

    @Resource
    MatchingCriteriaBean matchCriteria;

    @Override
    public UserClient saveOrUpdate(UserClient user) {
        user.setLogin(StringUtils.lowerCase(user.getLogin()));
        user.setEmail(StringUtils.lowerCase(user.getEmail()));
        user.setNormalizedName(matchCriteria.normalizedName(
                user.getFirstName(), user.getLastName(), user.getLogin(),
                user.getEmail()));
        return userClientRepository.save(user);
    }

    @Override
    public UserClient reload(UserClient userClient) {
        if (userClient == null || userClient.getId() == null) {
            return null;
        }
        return userClientRepository.findOne(userClient.getId());
    }

    @Override
    public Page<UserClient> list(Pageable pageable,
                                 UserClientSearchFilter filter) {
        return userClientRepository.findAll(
                where(userClientSpecifications.hasNames(filter))
                        .and(userClientSpecifications.isClient(filter))
                        .and(userClientSpecifications.isInStatus(filter))
                        .and(userClientSpecifications.hasRoleOnTeam(filter))
                        .and(userClientSpecifications.hasRoleOnTeamWithClientTag(filter)),
                (pageable == null) ? new PageRequest(0, 10, Sort.Direction.ASC, "id") : pageable);
    }

    @Override
    public Set<UserClient> findConflictingUsers(UserClient userClient) {
        if (userClient == null ||
                (StringUtils.isBlank(userClient.getLogin()) && StringUtils.isBlank(userClient.getEmail()))) {
            // nothing to search on
            return Collections.emptySet();
        }
        return new HashSet<>(
                userClientRepository.findAll(
                        where(userClientSpecifications.hasLogin(userClient))
                                .or(userClientSpecifications.hasEmail(userClient))
                                .and(userClientSpecifications.isNot(userClient))));
    }

    @Override
    public UserClient fetchUserWillFullPermissions(String currentLogin) {
        UserClient userClient = userClientRepository.findByLoginIgnoreCase(currentLogin);
        if (userClient != null) {
            userClient.getClientRoles().size();
            userClient.getTeamRoles().size();
        }
        return userClient;
    }

}
