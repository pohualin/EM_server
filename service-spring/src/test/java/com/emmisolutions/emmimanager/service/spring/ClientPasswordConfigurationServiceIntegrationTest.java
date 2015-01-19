package com.emmisolutions.emmimanager.service.spring;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
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
     * Test negative findByClient
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testFindByClientWithoutClient() {
        clientPasswordConfigurationService.get(null);
    }

    /**
     * Test negative findByClient
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testFindByClientWithoutClientId() {
        clientPasswordConfigurationService.get(new Client());
    }

    /**
     * Test findByClient
     */
    @Test
    public void testFindByClient() {
        // Test All Default
        Client client = makeNewRandomClient();
        Client newClient = new Client();
        newClient.setId(client.getId());
        ClientPasswordConfiguration allDefault = clientPasswordConfigurationService
                .get(newClient);

        assertThat("should contain passed in Client", allDefault.getClient(),
                is(client));

        assertThat("should contain system default password configuration",
                allDefault.getDefaultPasswordConfiguration().isSystemDefault(),
                is(true));

        assertThat(
                "should contain password configuration composed from default password configuration",
                allDefault.getDefaultPasswordConfiguration().getName() == allDefault
                        .getPasswordConfiguration().getName(), is(true));

        assertThat("should return null if no client found",
                clientPasswordConfigurationService.get(new Client(100l)),
                is(nullValue()));

    }

    /**
     * Test negative save
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testSaveWithoutClient() {
        ClientPasswordConfiguration configurationWithoutClient = new ClientPasswordConfiguration();
        clientPasswordConfigurationService.save(configurationWithoutClient);
    }

    /**
     * Test negative save
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testSaveWithoutClientId() {
        ClientPasswordConfiguration configurationWithoutClientId = new ClientPasswordConfiguration();
        configurationWithoutClientId.setClient(new Client());
        clientPasswordConfigurationService.save(configurationWithoutClientId);
    }

    /**
     * Test negative save
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testSaveWithoutDefaultPasswordConfiguration() {
        ClientPasswordConfiguration configurationWithoutDefaultPasswordConfiguration = new ClientPasswordConfiguration();
        configurationWithoutDefaultPasswordConfiguration
                .setClient(makeNewRandomClient());
        clientPasswordConfigurationService
                .save(configurationWithoutDefaultPasswordConfiguration);
    }

    /**
     * Test save
     */
    @Test
    public void testSave() {
        // Test save with system default DefaultPasswordConfiguration and
        // customized PasswordConfiguration
        Client client = makeNewRandomClient();
        ClientPasswordConfiguration configurationWithSystemDefault = clientPasswordConfigurationService
                .get(client);
        configurationWithSystemDefault.getPasswordConfiguration().setName(
                "Configuration with System Default");
        configurationWithSystemDefault = clientPasswordConfigurationService
                .save(configurationWithSystemDefault);

        assertThat(
                "should contain password configuration with new name",
                configurationWithSystemDefault.getPasswordConfiguration()
                        .getName()
                        .equalsIgnoreCase("Configuration with System Default"),
                is(true));

        ClientPasswordConfiguration updateSomeField = configurationWithSystemDefault;
        updateSomeField.getPasswordConfiguration().setName("A New Name");
        updateSomeField = clientPasswordConfigurationService
                .save(updateSomeField);
        assertThat("should contain password configuration with new name",
                updateSomeField.getPasswordConfiguration().getName()
                        .equalsIgnoreCase("A New Name"), is(true));

        ClientPasswordConfiguration reload = clientPasswordConfigurationService
                .reload(new ClientPasswordConfiguration(
                        configurationWithSystemDefault.getId()));
        assertThat(
                "reload should have latest name",
                reload.getPasswordConfiguration().getName()
                        .equalsIgnoreCase("A New Name"), is(true));

        clientPasswordConfigurationService.delete(reload);
    }

    /**
     * Test negative delete
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testBadDeleteWithoutClientPasswordConfiguration() {
        clientPasswordConfigurationService.delete(null);
    }

    /**
     * Test negative delete
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testBadDeleteWithoutClientPasswordConfigurationId() {
        clientPasswordConfigurationService
                .delete(new ClientPasswordConfiguration());
    }

    /**
     * Test negative delete
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testBadDeleteWithoutBadClientPasswordConfiguration() {
        ClientPasswordConfiguration toDelete = clientPasswordConfigurationService
                .get(makeNewRandomClient());
        clientPasswordConfigurationService.delete(toDelete);
    }

    /**
     * Test negative reload
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testBadReload() {
        clientPasswordConfigurationService.reload(null);
    }

    /**
     * Test negative reload
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testBadReloadWithoutId() {
        clientPasswordConfigurationService
                .reload(new ClientPasswordConfiguration());
    }
}
