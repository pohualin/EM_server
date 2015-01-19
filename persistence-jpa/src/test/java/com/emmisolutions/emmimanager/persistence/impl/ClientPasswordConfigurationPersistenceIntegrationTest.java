package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.ClientPasswordConfiguration;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientPasswordConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.DefaultPasswordConfigurationPersistence;

/**
 * Test ClientPasswordConfigurationPersistence
 */
public class ClientPasswordConfigurationPersistenceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    ClientPasswordConfigurationPersistence clientPasswordConfigurationPersistence;

    @Resource
    DefaultPasswordConfigurationPersistence defaultPasswordConfigurationPersistence;

    /**
     * Test negative reload
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testReload() {
        assertThat("bad reload",
                clientPasswordConfigurationPersistence.reload(null),
                is(nullValue()));
    }

    /**
     * Test negative save
     */
    @Test(expected = ConstraintViolationException.class)
    public void testSaveWithoutDefaultPasswordConfiguration() {
        ClientPasswordConfiguration configuration = new ClientPasswordConfiguration();
        configuration.setClient(makeNewRandomClient());
        configuration = clientPasswordConfigurationPersistence
                .saveOrUpdate(configuration);
        assertThat("save configuration successfully", configuration.getId(),
                is(notNullValue()));
    }

    /**
     * Test save Test reload
     */
    @Test
    public void testSave() {
        Client client = makeNewRandomClient();
        ClientPasswordConfiguration configuration = new ClientPasswordConfiguration();
        configuration.setClient(client);
        configuration
                .setDefaultPasswordConfiguration(defaultPasswordConfigurationPersistence
                        .reload(1l));
        configuration = clientPasswordConfigurationPersistence
                .saveOrUpdate(configuration);
        assertThat("save configuration successfully", configuration.getId(),
                is(notNullValue()));

        ClientPasswordConfiguration reload = clientPasswordConfigurationPersistence
                .reload(configuration.getId());
        assertThat("reload configuration successfully", reload.getId(),
                is(notNullValue()));
    }

    /**
     * Test findByClient
     */
    @Test
    public void testFindByClient() {
        Client client = makeNewRandomClient();
        ClientPasswordConfiguration configuration = new ClientPasswordConfiguration();
        configuration.setClient(client);
        configuration
                .setDefaultPasswordConfiguration(defaultPasswordConfigurationPersistence
                        .reload(1l));
        configuration = clientPasswordConfigurationPersistence
                .saveOrUpdate(configuration);

        ClientPasswordConfiguration findByClient = clientPasswordConfigurationPersistence
                .findByClient(client);
        assertThat("should find one ClientPasswordConfiguration by client",
                findByClient.getId(), is(notNullValue()));
        assertThat(
                "should find one ClientPasswordConfiguration with the passed in client",
                findByClient.getClient(), is(client));
    }
}
