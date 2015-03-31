package com.emmisolutions.emmimanager.service.spring;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emmisolutions.emmimanager.model.configuration.ClientPasswordConfiguration;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.UserClientPasswordHistory;
import com.emmisolutions.emmimanager.persistence.UserClientPasswordHistoryPersistence;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.service.ClientPasswordConfigurationService;
import com.emmisolutions.emmimanager.service.UserClientPasswordHistoryService;

/**
 * Service Implementation for UserClientPasswordHistoryService
 */
@Service
public class UserClientPasswordHistoryServiceImpl implements
        UserClientPasswordHistoryService {

    @Resource
    UserClientPersistence userClientPersistence;

    @Resource
    UserClientPasswordHistoryPersistence userClientPasswordHistoryPersistence;

    @Resource
    ClientPasswordConfigurationService clientPasswordConfigurationService;

    @Override
    @Transactional
    public void delete(UserClientPasswordHistory userClientPasswordHistory) {
        if (userClientPasswordHistory == null
                || userClientPasswordHistory.getId() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "UserClientPasswordHistory or userClientPasswordHistoryId cannot be null");
        }
        userClientPasswordHistoryPersistence.delete(userClientPasswordHistory
                .getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserClientPasswordHistory> get(Pageable pageable,
            UserClient userClient) {
        if (userClient == null || userClient.getId() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "UserClient or userClientId cannot be null");
        }
        return userClientPasswordHistoryPersistence.findByUserClientId(
                pageable, userClient.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public UserClientPasswordHistory reload(
            UserClientPasswordHistory userClientPasswordHistory) {
        if (userClientPasswordHistory == null
                || userClientPasswordHistory.getId() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "UserClientPasswordHistory or userClientPasswordHistoryId cannot be null");
        }
        return userClientPasswordHistoryPersistence
                .reload(userClientPasswordHistory.getId());
    }

    @Override
    @Transactional
    public UserClientPasswordHistory save(
            UserClientPasswordHistory userClientPasswordHistory) {
        userClientPasswordHistory.setUserClient(userClientPersistence
                .reload(userClientPasswordHistory.getUserClient()));
        return userClientPasswordHistoryPersistence
                .saveOrUpdate(userClientPasswordHistory);
    }

    @Override
    @Transactional
    public List<UserClientPasswordHistory> handleUserClientPasswordHistory(
            UserClient userClient) {
        UserClient fromDb = userClientPersistence.reload(userClient);
        if (fromDb == null) {
            throw new InvalidDataAccessApiUsageException(
                    "This method is only to be used with existing UserClient objects");
        }

        ClientPasswordConfiguration configuration = clientPasswordConfigurationService
                .get(fromDb.getClient());

        // Save latest
        UserClientPasswordHistory latest = new UserClientPasswordHistory();
        latest.setUserClient(fromDb);
        latest.setPassword(fromDb.getPassword());
        latest.setSalt(fromDb.getSalt());
        userClientPasswordHistoryPersistence.saveOrUpdate(latest);

        PageRequest pageRequest = new PageRequest(0,
                configuration.getPasswordRepetitions());
        Page<UserClientPasswordHistory> histories = get(pageRequest, fromDb);
        // purge oldest
        while (histories.hasContent() && histories.hasNext()) {
            histories = get(histories.nextPageable(), fromDb);
            for (UserClientPasswordHistory toPurge : histories.getContent()) {
                userClientPasswordHistoryPersistence.delete(toPurge.getId());
            }
        }

        return histories.getContent();
    }

}
