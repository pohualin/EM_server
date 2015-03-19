package com.emmisolutions.emmimanager.service.spring;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

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
    @Transactional
    public List<UserClientPasswordHistory> get(UserClient userClient) {
        if (userClient == null || userClient.getId() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "UserClient or userClientId cannot be null");
        }
        return userClientPasswordHistoryPersistence
                .findByUserClientId(userClient.getId());
    }

    @Override
    @Transactional
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
    public void handleUserClientPasswordHistory(UserClient userClient) {
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
        latest.setPasswordSavedTime(fromDb.getPasswordSavedDateTime());
        userClientPasswordHistoryPersistence.saveOrUpdate(latest);
        
        List<UserClientPasswordHistory> histories = get(fromDb);
        // purge oldest
        if (histories.size() > configuration.getPasswordRepetitions()) {
            List<UserClientPasswordHistory> toPurgeList = histories.subList(
                    configuration.getPasswordRepetitions(),
                    histories.size());
            for(UserClientPasswordHistory toPurge: toPurgeList){
                userClientPasswordHistoryPersistence.delete(toPurge.getId());
            }
        }
        
    }

}
