package com.emmisolutions.emmimanager.service.spring;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.ClientPasswordConfiguration;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientPasswordConfigurationService;

/**
 * Test Service Implementation for ClientPasswordConfiguration
 */
public class ClientPasswordConfigurationServiceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    ClientPasswordConfigurationService clientPasswordConfigurationService;

    /**
     * Test get, save, reload, delete
     */
    @Test
    public void testSave() {
        Client client = makeNewRandomClient();
        ClientPasswordConfiguration configuration = clientPasswordConfigurationService
                .get(client);
        assertThat("should contain default password configuration",
                configuration.getDefaultPasswordConfiguration().isActive(),
                is(true));
        assertThat("should contain password configuration with default value",
                configuration.getName(), is("System Default"));

        configuration.setName("Configuration with New Name");
        configuration = clientPasswordConfigurationService.save(configuration);

        assertThat("should contain password configuration with new name",
                configuration.getName(), is("Configuration with New Name"));

        ClientPasswordConfiguration reload = clientPasswordConfigurationService
                .reload(new ClientPasswordConfiguration(configuration.getId()));
        ClientPasswordConfiguration getInserted = clientPasswordConfigurationService
                .get(client);
        assertThat("reload the same instance", reload.getId(),
                is(configuration.getId()));
        assertThat("reload and get the same instance", reload.getId(),
                is(getInserted.getId()));

        clientPasswordConfigurationService.delete(reload);
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeDeleteWithNull() {
        clientPasswordConfigurationService.delete(null);
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeDeleteWithNullId() {
        clientPasswordConfigurationService
                .delete(new ClientPasswordConfiguration());
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeReloadWithNull() {
        clientPasswordConfigurationService.reload(null);
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeReloadWithNullId() {
        clientPasswordConfigurationService
                .reload(new ClientPasswordConfiguration());
    }

}
