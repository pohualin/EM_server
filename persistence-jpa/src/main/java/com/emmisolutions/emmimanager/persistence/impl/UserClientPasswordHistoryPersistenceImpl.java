package com.emmisolutions.emmimanager.persistence.impl;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.user.client.UserClientPasswordHistory;
import com.emmisolutions.emmimanager.persistence.UserClientPasswordHistoryPersistence;
import com.emmisolutions.emmimanager.persistence.repo.UserClientPasswordHistoryRepository;

/**
 * Persistence Implementation to deal with PasswordConfiguration
 */
@Repository
public class UserClientPasswordHistoryPersistenceImpl implements
        UserClientPasswordHistoryPersistence {

    @Resource
    UserClientPasswordHistoryRepository userClientPasswordHistoryRepository;

    @Override
    public UserClientPasswordHistory reload(Long id) {
        return userClientPasswordHistoryRepository.findOne(id);
    }

    @Override
    public UserClientPasswordHistory saveOrUpdate(
            UserClientPasswordHistory userClientPasswordHistory) {
        return userClientPasswordHistoryRepository
                .save(userClientPasswordHistory);
    }

    @Override
    public void delete(Long id) {
        UserClientPasswordHistory toDelete = reload(id);
        if (toDelete != null) {
            userClientPasswordHistoryRepository.delete(toDelete);
        }
    }

    @Override
    public Page<UserClientPasswordHistory> findByUserClientId(
            Pageable pageable, Long id) {
        Pageable toUse = pageable;
        if (toUse == null) {
            toUse = new PageRequest(0, 20);
        }
        return userClientPasswordHistoryRepository
                .findByUserClientIdOrderByCreatedDateDesc(toUse, id);
    }

}
