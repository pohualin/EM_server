package com.emmisolutions.emmimanager.service.spring;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

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

    /**
     * Test value out of range
     */
    @Test
    public void testOutofRange() {
        Client client = makeNewRandomClient();
        ClientPasswordConfiguration passwordConfig = clientPasswordConfigurationService
                .get(client);

        // greater than max
        try {
            passwordConfig.setPasswordExpirationDays(200);
            clientPasswordConfigurationService.save(passwordConfig);
            fail("did not throw when it should");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        // less than min
        try {
            passwordConfig.setPasswordExpirationDays(0);
            clientPasswordConfigurationService.save(passwordConfig);
            fail("did not throw when it should");
        } catch (InvalidDataAccessApiUsageException e) {
        }
        
        // in range
        passwordConfig.setPasswordExpirationDays(180);
        passwordConfig = clientPasswordConfigurationService.save(passwordConfig);
        passwordConfig.setPasswordExpirationDays(1);
        passwordConfig = clientPasswordConfigurationService.save(passwordConfig);
        
        // greater than max
        try {
            passwordConfig.setPasswordExpirationDaysReminder(200);
            clientPasswordConfigurationService.save(passwordConfig);
            fail("did not throw when it should");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        // less than min
        try {
            passwordConfig.setPasswordExpirationDaysReminder(0);
            clientPasswordConfigurationService.save(passwordConfig);
            fail("did not throw when it should");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        // in range
        passwordConfig.setPasswordExpirationDaysReminder(30);
        passwordConfig = clientPasswordConfigurationService.save(passwordConfig);
        passwordConfig.setPasswordExpirationDaysReminder(5);
        passwordConfig = clientPasswordConfigurationService.save(passwordConfig);
        
        // greater than max
        try {
            passwordConfig.setPasswordLength(200);
            clientPasswordConfigurationService.save(passwordConfig);
            fail("did not throw when it should");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        // less than min
        try {
            passwordConfig.setPasswordLength(0);
            clientPasswordConfigurationService.save(passwordConfig);
            fail("did not throw when it should");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        // in range
        passwordConfig.setPasswordLength(50);
        passwordConfig = clientPasswordConfigurationService.save(passwordConfig);
        passwordConfig.setPasswordLength(8);
        passwordConfig = clientPasswordConfigurationService.save(passwordConfig);
        
        // greater than max
        try {
            passwordConfig.setPasswordRepetitions(200);
            clientPasswordConfigurationService.save(passwordConfig);
            fail("did not throw when it should");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        // less than min
        try {
            passwordConfig.setPasswordRepetitions(0);
            clientPasswordConfigurationService.save(passwordConfig);
            fail("did not throw when it should");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        // in range
        passwordConfig.setPasswordRepetitions(20);
        passwordConfig = clientPasswordConfigurationService.save(passwordConfig);
        passwordConfig.setPasswordRepetitions(3);
        passwordConfig = clientPasswordConfigurationService.save(passwordConfig);
 
        // greater than max
        try {
            passwordConfig.setDaysBetweenPasswordChange(200);
            clientPasswordConfigurationService.save(passwordConfig);
            fail("did not throw when it should");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        // less than min
        try {
            passwordConfig.setDaysBetweenPasswordChange(0);
            clientPasswordConfigurationService.save(passwordConfig);
            fail("did not throw when it should");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        // in range
        passwordConfig.setDaysBetweenPasswordChange(50);
        passwordConfig = clientPasswordConfigurationService.save(passwordConfig);
        passwordConfig.setDaysBetweenPasswordChange(1);
        passwordConfig = clientPasswordConfigurationService.save(passwordConfig);
        
        // greater than max
        try {
            passwordConfig.setIdleTime(200);
            clientPasswordConfigurationService.save(passwordConfig);
            fail("did not throw when it should");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        // less than min
        try {
            passwordConfig.setIdleTime(0);
            clientPasswordConfigurationService.save(passwordConfig);
            fail("did not throw when it should");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        // in range
        passwordConfig.setIdleTime(90);
        passwordConfig = clientPasswordConfigurationService.save(passwordConfig);
        passwordConfig.setIdleTime(1);
        passwordConfig = clientPasswordConfigurationService.save(passwordConfig);
        
        // greater than max
        try {
            passwordConfig.setLockoutAttemps(200);
            clientPasswordConfigurationService.save(passwordConfig);
            fail("did not throw when it should");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        // less than min
        try {
            passwordConfig.setLockoutAttemps(0);
            clientPasswordConfigurationService.save(passwordConfig);
            fail("did not throw when it should");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        // in range
        passwordConfig.setLockoutAttemps(50);
        passwordConfig = clientPasswordConfigurationService.save(passwordConfig);
        passwordConfig.setLockoutAttemps(3);
        passwordConfig = clientPasswordConfigurationService.save(passwordConfig);
        
        // greater than max
        try {
            passwordConfig.setLockoutReset(200);
            clientPasswordConfigurationService.save(passwordConfig);
            fail("did not throw when it should");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        // less than min
        try {
            passwordConfig.setLockoutReset(-1);
            clientPasswordConfigurationService.save(passwordConfig);
            fail("did not throw when it should");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        // in range
        passwordConfig.setLockoutReset(120);
        passwordConfig = clientPasswordConfigurationService.save(passwordConfig);
        passwordConfig.setLockoutReset(0);
        passwordConfig = clientPasswordConfigurationService.save(passwordConfig);
    }

}
