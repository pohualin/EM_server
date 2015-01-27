package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.IpRestrictConfiguration;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientRestrictConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.IpRestrictConfigurationPersistence;

/**
 * Integration test for IpRestrictConfigurationPersistence
 */
public class IpRestrictConfigurationPersistenceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    ClientRestrictConfigurationPersistence clientRestrictConfigurationPersistence;

    @Resource
    IpRestrictConfigurationPersistence ipRestrictConfigurationPersistence;

    /**
     * Test list
     */
    @Test
    public void testList() {
        Client client = makeNewRandomClient();

        IpRestrictConfiguration configuration = new IpRestrictConfiguration();
        configuration.setClient(client);
        configuration.setDescription(RandomStringUtils.randomAlphabetic(255));
        configuration.setIpRangeEnd("192.168.1.255");
        configuration.setIpRangeStart("192.168.1.1");
        configuration = ipRestrictConfigurationPersistence
                .saveOrUpdate(configuration);

        IpRestrictConfiguration configurationA = new IpRestrictConfiguration();
        configurationA.setClient(client);
        configurationA.setDescription(RandomStringUtils.randomAlphabetic(255));
        configurationA.setIpRangeEnd("192.170.1.255");
        configurationA.setIpRangeStart("192.170.1.1");
        configurationA = ipRestrictConfigurationPersistence
                .saveOrUpdate(configuration);

        Page<IpRestrictConfiguration> listOfIpConfig = ipRestrictConfigurationPersistence
                .list(new PageRequest(0, 10), client.getId());

        assertThat("should contain configuration", listOfIpConfig.getContent(),
                hasItem(configuration));
        assertThat("should contain configurationA",
                listOfIpConfig.getContent(), hasItem(configurationA));

        Page<IpRestrictConfiguration> listOfIpConfigA = ipRestrictConfigurationPersistence
                .list(null, client.getId());

        assertThat("should contain configuration",
                listOfIpConfigA.getContent(), hasItem(configuration));
        assertThat("should contain configurationA",
                listOfIpConfigA.getContent(), hasItem(configurationA));

        Page<IpRestrictConfiguration> listOfIpConfigB = ipRestrictConfigurationPersistence
                .list(new PageRequest(0, 10), null);
        assertThat("should contain configuration",
                listOfIpConfigB.getContent(), hasItem(configuration));
        assertThat("should contain configurationA",
                listOfIpConfigB.getContent(), hasItem(configurationA));
    }

    /**
     * Test save, reload, update and delete
     */
    @Test
    public void testSave() {
        Client client = makeNewRandomClient();

        IpRestrictConfiguration configuration = new IpRestrictConfiguration();
        configuration.setClient(client);
        configuration.setDescription(RandomStringUtils.randomAlphabetic(255));
        configuration.setIpRangeEnd("192.168.1.255");
        configuration.setIpRangeStart("192.168.1.1");
        configuration = ipRestrictConfigurationPersistence
                .saveOrUpdate(configuration);

        assertThat("IpRestrictConfiguration should be saved.",
                configuration.getId(), is(notNullValue()));

        assertThat(
                "IpRestrictConfiguration should be saved with ip range start.",
                configuration.getIpRangeStart(), is("192.168.1.1"));

        IpRestrictConfiguration reload = ipRestrictConfigurationPersistence
                .reload(configuration.getId());
        assertThat("should reload the same configuration",
                configuration.getId() == reload.getId(), is(true));

        reload.setIpRangeStart("192.168.1.0");
        IpRestrictConfiguration update = ipRestrictConfigurationPersistence
                .saveOrUpdate(reload);
        assertThat("should update the ip config start",
                update.getIpRangeStart(), is("192.168.1.0"));

        ipRestrictConfigurationPersistence.delete(configuration.getId());
        IpRestrictConfiguration reloadAfterDelete = ipRestrictConfigurationPersistence
                .reload(configuration.getId());
        assertThat("should reload the same configuration",
                configuration.getId() == reload.getId(), is(true));
    }
}
