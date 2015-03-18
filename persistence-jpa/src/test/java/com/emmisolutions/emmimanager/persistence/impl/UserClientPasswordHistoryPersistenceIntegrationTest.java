package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.UserClientPasswordHistory;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.UserClientPasswordHistoryPersistence;

/**
 * Test ClientPasswordConfigurationPersistence
 */
public class UserClientPasswordHistoryPersistenceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    UserClientPasswordHistoryPersistence userClientPasswordHistoryPersistence;

    /**
     * Test negative reload
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testReload() {
        assertThat("bad reload",
                userClientPasswordHistoryPersistence.reload(null),
                is(nullValue()));
    }

    /**
     * Test save, reload and delete
     */
    @Test
    public void testSave() {
        UserClient userClient = makeNewRandomUserClient(null);

        UserClientPasswordHistory history = new UserClientPasswordHistory();
        history.setUserClient(userClient);
        history.setPasswordSavedTime(LocalDateTime.now().minusDays(1));
        history.setPassword("password");
        history.setSalt("salt");
        history = userClientPasswordHistoryPersistence.saveOrUpdate(history);
        assertThat("save configuration successfully", history.getId(),
                is(notNullValue()));

        UserClientPasswordHistory reload = userClientPasswordHistoryPersistence
                .reload(history.getId());
        assertThat("reload configuration successfully", reload.getId(),
                is(notNullValue()));

        userClientPasswordHistoryPersistence.delete(reload.getId());
        assertThat("reload configuration successfully",
                userClientPasswordHistoryPersistence.reload(reload.getId()),
                is(nullValue()));
    }

    /**
     * Test findByUserClientId
     */
    @Test
    public void findByUserClientId() {
        UserClient userClient = makeNewRandomUserClient(null);

        UserClientPasswordHistory history = new UserClientPasswordHistory();
        history.setUserClient(userClient);
        history.setPasswordSavedTime(LocalDateTime.now().minusDays(1));
        history.setPassword("password");
        history.setSalt("salt");
        history = userClientPasswordHistoryPersistence.saveOrUpdate(history);
        assertThat("save configuration successfully", history.getId(),
                is(notNullValue()));

        UserClientPasswordHistory history2 = new UserClientPasswordHistory();
        history2.setUserClient(userClient);
        history2.setPasswordSavedTime(LocalDateTime.now().minusDays(2));
        history2.setPassword("password2");
        history2.setSalt("salt2");
        history2 = userClientPasswordHistoryPersistence.saveOrUpdate(history2);
        assertThat("save configuration successfully", history2.getId(),
                is(notNullValue()));

        assertThat("password histories contains history",
                userClientPasswordHistoryPersistence
                        .findByUserClientId(userClient.getId()),
                hasItem(history));

        assertThat("password histories contains history2",
                userClientPasswordHistoryPersistence
                        .findByUserClientId(userClient.getId()),
                hasItem(history2));
        
        assertThat("latest password in histories is history",
                userClientPasswordHistoryPersistence
                        .findByUserClientId(userClient.getId()).get(0),
                is(history));
    }

}
