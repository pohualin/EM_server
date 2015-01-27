package com.emmisolutions.emmimanager.service.spring;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.ClientRestrictConfiguration;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientRestrictConfigurationService;

/**
 * An integration test for ClientRestrictConfigurationServiceImpl
 */
public class ClientRestrictConfigurationServiceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    ClientRestrictConfigurationService clientRestrictConfigurationService;

    /**
     * Test CRUD
     */
    @Test
    public void testCreateReloadUpdateDelete() {
        ClientRestrictConfiguration config = new ClientRestrictConfiguration();
        config.setClient(makeNewRandomClient());
        config = clientRestrictConfigurationService.create(config);

        assertThat("config saved with id", config.getId(), is(notNullValue()));

        ClientRestrictConfiguration reload = new ClientRestrictConfiguration(
                config.getId());
        reload = clientRestrictConfigurationService.reload(reload);
        assertThat("reload should be the same instance", reload.getId(),
                is(config.getId()));

        ClientRestrictConfiguration configToUpdate = reload;
        configToUpdate.setIpConfigRestrict(false);
        configToUpdate.setEmailConfigRestrict(true);
        configToUpdate = clientRestrictConfigurationService
                .update(configToUpdate);

        assertThat("config updated with same id",
                config.getId() == configToUpdate.getId(), is(true));
        assertThat("updatec donfig with ipConfigRestrict equals to false",
                configToUpdate.isIpConfigRestrict(), is(false));

        clientRestrictConfigurationService.delete(configToUpdate);
        assertThat("Should load nothing after delete",
                clientRestrictConfigurationService.reload(configToUpdate),
                is(nullValue()));
    }

    /**
     * Test getByClient
     */
    @Test
    public void testGetByClient() {
        Client client = makeNewRandomClient();
        ClientRestrictConfiguration config = new ClientRestrictConfiguration();
        config.setClient(client);
        config.setIpConfigRestrict(true);
        config.setEmailConfigRestrict(true);
        config = clientRestrictConfigurationService.create(config);

        ClientRestrictConfiguration configFound = clientRestrictConfigurationService
                .getByClient(client);
        assertThat("Should find the config with client",
                configFound.getClient(), is(client));

        assertThat("Should not find any config with different client",
                clientRestrictConfigurationService
                        .getByClient(makeNewRandomClient()), is(nullValue()));
    }

    /**
     * Test negative delete
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeDeleteWithNull() {
        clientRestrictConfigurationService.delete(null);
    }

    /**
     * Test negative delete
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeDeleteWithNullId() {
        clientRestrictConfigurationService
                .delete(new ClientRestrictConfiguration());
    }

    /**
     * Test negative reload
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeReloadWithNull() {
        clientRestrictConfigurationService.reload(null);
    }

    /**
     * Test negative reload
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeReloadWithNullId() {
        clientRestrictConfigurationService
                .reload(new ClientRestrictConfiguration());
    }

    /**
     * Test negative update
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeUpdateWithNull() {
        clientRestrictConfigurationService.update(null);
    }

    /**
     * Test negative update
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeUpdateWithNullId() {
        clientRestrictConfigurationService
                .update(new ClientRestrictConfiguration());
    }
}
