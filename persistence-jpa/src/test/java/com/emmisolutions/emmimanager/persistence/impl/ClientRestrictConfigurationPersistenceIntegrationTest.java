package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.junit.Test;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientRestrictConfiguration;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientRestrictConfigurationPersistence;

/**
 * Integration test for ClientRestrictConfigurationPersistence
 */
public class ClientRestrictConfigurationPersistenceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    ClientRestrictConfigurationPersistence clientRestrictConfigurationPersistence;

    /**
     * Test findByClient
     */
    @Test
    public void testFindByClient() {
        Client client = makeNewRandomClient();
        ClientRestrictConfiguration configuration = new ClientRestrictConfiguration();
        configuration.setClient(client);
        configuration.setIpConfigRestrict(true);
        configuration.setEmailConfigRestrict(true);
        configuration = clientRestrictConfigurationPersistence
                .saveOrUpdate(configuration);

        assertThat("ClientRestrictConfiguration should be saved.",
                configuration.getId(), is(notNullValue()));

        ClientRestrictConfiguration findByClient = clientRestrictConfigurationPersistence
                .findByClient(client);
        assertThat("should found the configuration for client.",
                configuration.getId() == findByClient.getId(), is(true));

    }

    /**
     * Test save, reload and delete
     */
    @Test
    public void testSave() {
        ClientRestrictConfiguration configuration = new ClientRestrictConfiguration();
        configuration.setClient(makeNewRandomClient());
        configuration.setIpConfigRestrict(true);
        configuration.setEmailConfigRestrict(true);
        configuration = clientRestrictConfigurationPersistence
                .saveOrUpdate(configuration);

        assertThat("ClientRestrictConfiguration should be saved.",
                configuration.getId(), is(notNullValue()));

        ClientRestrictConfiguration reload = clientRestrictConfigurationPersistence
                .reload(configuration.getId());
        assertThat("should reload the same configuration.",
                configuration.getId() == reload.getId(), is(true));

        clientRestrictConfigurationPersistence.delete(reload.getId());
        ClientRestrictConfiguration reloadAfterDelete = clientRestrictConfigurationPersistence
                .reload(configuration.getId());
        assertThat("should reload nothing", reloadAfterDelete, is(nullValue()));
    }
}
