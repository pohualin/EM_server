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

import com.emmisolutions.emmimanager.model.configuration.ClientRestrictConfiguration;
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
        ClientRestrictConfiguration config = new ClientRestrictConfiguration();
        config.setClient(makeNewRandomClient());
        config = clientRestrictConfigurationService.create(config);

        assertThat("config saved with id", config.getId(), is(notNullValue()));

        IpRestrictConfiguration ipConfig = new IpRestrictConfiguration();
        ipConfig.setClientRestrictConfiguration(config);
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
        ClientRestrictConfiguration config = new ClientRestrictConfiguration();
        config.setClient(makeNewRandomClient());
        config = clientRestrictConfigurationService.create(config);

        IpRestrictConfiguration ipConfig = new IpRestrictConfiguration();
        ipConfig.setClientRestrictConfiguration(config);
        ipConfig.setIpRangeStart("192.168.1.1");
        ipConfig.setIpRangeEnd("192.168.1.254");
        ipConfig.setDescription(RandomStringUtils.randomAlphabetic(255));
        ipConfig = ipRestrictConfigurationService.create(ipConfig);

        IpRestrictConfiguration ipConfigA = new IpRestrictConfiguration();
        ipConfigA.setClientRestrictConfiguration(config);
        ipConfigA.setIpRangeStart("192.68.1.1");
        ipConfigA.setIpRangeEnd("192.68.1.254");
        ipConfigA.setDescription(RandomStringUtils.randomAlphabetic(255));
        ipConfigA = ipRestrictConfigurationService.create(ipConfigA);

        Page<IpRestrictConfiguration> page = ipRestrictConfigurationService
                .getByClientRestrictConfiguration(null, config);

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
        ipRestrictConfigurationService.getByClientRestrictConfiguration(null,
                null);
    }

    /**
     * Test bad Get
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeGetNullId() {
        ipRestrictConfigurationService.getByClientRestrictConfiguration(null,
                new ClientRestrictConfiguration());
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
