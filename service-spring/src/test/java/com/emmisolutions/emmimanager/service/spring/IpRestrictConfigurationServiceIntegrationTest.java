package com.emmisolutions.emmimanager.service.spring;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.IpRestrictConfiguration;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientRestrictConfigurationService;
import com.emmisolutions.emmimanager.service.IpRestrictConfigurationService;

/**
 * An integration test for IpRestrictConfigurationServiceImpl
 */
public class IpRestrictConfigurationServiceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    ClientRestrictConfigurationService clientRestrictConfigurationService;

    @Resource
    IpRestrictConfigurationService ipRestrictConfigurationService;

    /**
     * Test CRUD
     */
    @Test
    public void testCreateReloadUpdateDelete() {
        Client client = makeNewRandomClient();

        IpRestrictConfiguration ipConfig = new IpRestrictConfiguration();
        ipConfig.setClient(client);
        ipConfig.setIpRangeStart("192.168.1.1");
        ipConfig.setIpRangeEnd("192.168.1.254");
        ipConfig.setDescription(RandomStringUtils.randomAlphabetic(255));
        ipConfig = ipRestrictConfigurationService.create(ipConfig);

        assertThat("ipConfig saved with id", ipConfig.getId(),
                is(notNullValue()));

        IpRestrictConfiguration reloadIpConfig = ipRestrictConfigurationService
                .reload(new IpRestrictConfiguration(ipConfig.getId()));

        assertThat("should reload the same ip configuration",
                reloadIpConfig.getId(), is(ipConfig.getId()));

        IpRestrictConfiguration updateIpConfig = reloadIpConfig;
        updateIpConfig.setIpRangeEnd("192.168.1.253");
        updateIpConfig = ipRestrictConfigurationService.update(updateIpConfig);

        assertThat("should update the existing ip configuration",
                updateIpConfig.getId(), is(reloadIpConfig.getId()));
        assertThat("should have the updated ip ending",
                updateIpConfig.getIpRangeEnd(), is("192.168.1.253"));

        ipRestrictConfigurationService.delete(updateIpConfig);
    }

    /**
     * Test getByClientRestrictConfiguration
     */
    @Test
    public void testGetByClientRestrictConfiguration() {
        Client client = makeNewRandomClient();

        IpRestrictConfiguration ipConfig = new IpRestrictConfiguration();
        ipConfig.setClient(client);
        ipConfig.setIpRangeStart("192.168.1.1");
        ipConfig.setIpRangeEnd("192.168.1.254");
        ipConfig.setDescription(RandomStringUtils.randomAlphabetic(255));
        ipConfig = ipRestrictConfigurationService.create(ipConfig);

        IpRestrictConfiguration ipConfigA = new IpRestrictConfiguration();
        ipConfigA.setClient(client);
        ipConfigA.setIpRangeStart("192.68.1.1");
        ipConfigA.setIpRangeEnd("192.68.1.254");
        ipConfigA.setDescription(RandomStringUtils.randomAlphabetic(255));
        ipConfigA = ipRestrictConfigurationService.create(ipConfigA);

        Page<IpRestrictConfiguration> page = ipRestrictConfigurationService
                .getByClient(null, client);

        assertThat("return page should include ip config", page.getContent(),
                hasItem(ipConfig));

        assertThat("return page should include another ip config",
                page.getContent(), hasItem(ipConfigA));

    }

    /**
     * Test bad Delete
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeDeleteNull() {
        ipRestrictConfigurationService.delete(null);
    }

    /**
     * Test bad Delete
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeDeleteNullId() {
        ipRestrictConfigurationService.delete(new IpRestrictConfiguration());
    }

    /**
     * Test bad Reload
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeReloadNull() {
        ipRestrictConfigurationService.reload(null);
    }

    /**
     * Test bad Reload
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeReloadNullId() {
        ipRestrictConfigurationService.reload(new IpRestrictConfiguration());
    }

    /**
     * Test bad Get
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeGetNull() {
        ipRestrictConfigurationService.getByClient(null, null);
    }

    /**
     * Test bad Get
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeGetNullId() {
        ipRestrictConfigurationService.getByClient(null, new Client());
    }

    /**
     * Test bad Update
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeUpdateNull() {
        ipRestrictConfigurationService.update(null);
    }

    /**
     * Test bad Update
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeUpdateNullId() {
        ipRestrictConfigurationService.update(new IpRestrictConfiguration());
    }

}
