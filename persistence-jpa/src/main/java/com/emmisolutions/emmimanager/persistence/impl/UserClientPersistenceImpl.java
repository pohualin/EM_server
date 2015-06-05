package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.UserClientSearchFilter;
import com.emmisolutions.emmimanager.model.configuration.EmailRestrictConfiguration;
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
import java.util.*;

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
        // make sure null is saved for blank email
        user.setEmail(StringUtils.stripToNull(user.getEmail()));
        user.setEmailValidated(user.isEmailValidated());
        user.setNotNowExpirationTime(user.getNotNowExpirationTime());
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
                        .and(userClientSpecifications.hasRoleOnTeamWithClientTag(filter))
                        .and(userClientSpecifications.orEmailEndingsForClient(filter)),
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
        return userClientRepository.findByLoginIgnoreCase(currentLogin);
    }

    @Override
    public UserClient findByActivationKey(String activationKey) {
        if (StringUtils.isNotBlank(activationKey)) {
            return userClientRepository.findByActivationKey(activationKey);
        }
        return null;
    }

    @Override
    public UserClient findByResetToken(String resetToken) {
        if (StringUtils.isNotBlank(resetToken)) {
            return userClientRepository.findByPasswordResetToken(resetToken);
        }
        return null;
    }

    @Override
    public UserClient findByValidationToken(String validationToken) {
        if (StringUtils.isNotBlank(validationToken)) {
            return userClientRepository.findByValidationToken(validationToken);
        }
        return null;
    }

    @Override
    public UserClient findByEmail(String email) {
        if (StringUtils.isNotBlank(email)) {
            return userClientRepository.findByEmailIgnoreCase(email);
        }
        return null;
    }

    @Override
    public UserClient unlockUserClient(UserClient userClient) {
        UserClient toUnlock = userClient;
        toUnlock.setAccountNonLocked(true);
        toUnlock.setLoginFailureCount(0);
        toUnlock.setLockExpirationDateTime(null);
        return userClientRepository.save(toUnlock);
    }
}
