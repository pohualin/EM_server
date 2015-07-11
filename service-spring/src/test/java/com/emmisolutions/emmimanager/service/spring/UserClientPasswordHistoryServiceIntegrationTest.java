package com.emmisolutions.emmimanager.service.spring;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.ClientPasswordConfiguration;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.UserClientPasswordHistory;
import com.emmisolutions.emmimanager.persistence.repo.UserClientPasswordHistoryRepository;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientPasswordConfigurationService;
import com.emmisolutions.emmimanager.service.UserClientPasswordHistoryService;
import com.emmisolutions.emmimanager.service.UserClientPasswordService;

/**
 * Test Service Implementation for UserClientPasswordHistory
 */
public class UserClientPasswordHistoryServiceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    UserClientPasswordService userClientPasswordService;

    @Resource
    UserClientPasswordHistoryService userClientPasswordHistoryService;
    
    @Resource
    UserClientPasswordHistoryRepository userClientPasswordHistoryRepository;

    @Resource
    ClientPasswordConfigurationService clientPasswordConfigurationService;

    /**
     * Test get, save, reload, delete
     */
    @Test
    public void testSave() {
        UserClient userClient = makeNewRandomUserClient(null);

        UserClientPasswordHistory history = new UserClientPasswordHistory();
        history.setUserClient(userClient);
        history.setPassword("password");
        history.setSalt("salt");
        history = userClientPasswordHistoryService.save(history);

        assertThat("save an UserClientPasswordHistory successfully",
                history.getId(), is(notNullValue()));

        UserClientPasswordHistory reload = new UserClientPasswordHistory(
                history.getId());
        UserClientPasswordHistory reloaded = userClientPasswordHistoryService.reload(reload);
        assertThat("reload the same instance",
                history.getId() == reloaded.getId(), is(true));

        Page<UserClientPasswordHistory> historiesWithNullPageable = userClientPasswordHistoryService
                .get(null, new UserClient(userClient.getId()));
        assertThat("historiesWithNullPageable contains history",
                historiesWithNullPageable.getContent(), hasItem(history));

        Page<UserClientPasswordHistory> histories = userClientPasswordHistoryService
                .get(new PageRequest(0, 10), new UserClient(userClient.getId()));
        assertThat("histories contains history", histories.getContent(),
                hasItem(history));

        userClientPasswordHistoryService.delete(history);
        UserClientPasswordHistory reloadAfterDelete = userClientPasswordHistoryService.reload(reload);
        assertThat("nothing should be returned", reloadAfterDelete, is(nullValue()));

    }

    @Test
    public void testBad() {
        UserClient userClient = makeNewRandomUserClient(null);

        UserClientPasswordHistory history = new UserClientPasswordHistory();
        history.setUserClient(userClient);
        history.setPassword("password");
        history.setSalt("salt");
        history = userClientPasswordHistoryService.save(history);

        try {
            userClientPasswordHistoryService.reload(null);
            fail("can not reload null");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        try {
            userClientPasswordHistoryService
                    .reload(new UserClientPasswordHistory());
            fail("can not reload with null id");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        try {
            userClientPasswordHistoryService.delete(null);
            fail("can not delete null");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        try {
            userClientPasswordHistoryService
                    .delete(new UserClientPasswordHistory());
            fail("can not delete null id");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        try {
            userClientPasswordHistoryService.get(null, null);
            fail("can not get history with null UserClient");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        try {
            userClientPasswordHistoryService.get(null, new UserClient());
            fail("can not get history with null userClientId");
        } catch (InvalidDataAccessApiUsageException e) {
        }

    }

    @Test
    public void handlePasswordHistory() {
        UserClient userClient = makeNewRandomUserClient(null);
        userClient.setPassword("password3");
        userClient = userClientPasswordService
                .updatePassword(userClient, false);
        userClient = userClientPasswordService
                .updatePasswordExpirationTime(userClient);
        Client client = userClient.getClient();
        ClientPasswordConfiguration configuration = makeNewRandomClientPasswordConfiguration(client);

        // Client does not allow past one password
        configuration.setPasswordRepetitions(3);
        configuration = clientPasswordConfigurationService.save(configuration);

        UserClientPasswordHistory history1 = new UserClientPasswordHistory();
        history1.setUserClient(userClient);
        history1.setPassword("password1");
        history1.setSalt("salt1");
        history1 = userClientPasswordHistoryService.save(history1);
        
        UserClientPasswordHistory history2 = new UserClientPasswordHistory();
        history2.setUserClient(userClient);
        history2.setPassword("password2");
        history2.setSalt("salt2");
        history2 = userClientPasswordHistoryService.save(history2);
        
        UserClientPasswordHistory history3 = new UserClientPasswordHistory();
        history3.setUserClient(userClient);
        history3.setPassword("password3");
        history3.setSalt("salt3");
        history3 = userClientPasswordHistoryService.save(history3);

        try {
            userClientPasswordHistoryService
                    .handleUserClientPasswordHistory(new UserClient());
            fail("This method should only used by existing UserClient");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        userClientPasswordHistoryService
                .handleUserClientPasswordHistory(userClient);

        Page<UserClientPasswordHistory> histories = userClientPasswordHistoryService
                .get(null, userClient);

        assertThat("histories should not contain password history1",
                histories.getContent(), not(hasItem(history1)));

    }
}
