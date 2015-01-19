package com.emmisolutions.emmimanager.persistence.impl;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.emmisolutions.emmimanager.model.configuration.PasswordConfiguration;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.PasswordConfigurationPersistence;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Test PasswordConfigurationPersistence
 */
public class PasswordConfigurationPersistenceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    PasswordConfigurationPersistence passwordConfigurationPersistence;

    /**
     * Test negative reload
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testBadReload() {
        assertThat("bad reload", passwordConfigurationPersistence.reload(null),
                is(nullValue()));
    }

    /**
     * Test Save 
     * Test Reload
     */
    @Test
    public void testSave() {
        PasswordConfiguration configuration = new PasswordConfiguration();
        configuration.setName("Configuration For Client A");
        configuration.setPasswordExpirationDays(180);
        configuration.setPasswordRepetitions(10);
        configuration.setDaysBetweenPasswordChange(10);
        configuration.setPasswordLength(10);
        configuration.setUppercaseLetters(true);
        configuration.setLowercaseLetters(true);
        configuration.setNumbers(true);
        configuration.setSpecialChars(true);
        configuration.setLockoutAttemps(10);
        configuration.setLockoutReset(10);
        configuration.setIdleTime(10);
        configuration.setPasswordExpirationDaysReminder(10);
        configuration = passwordConfigurationPersistence
                .saveOrUpdate(configuration);
        assertThat("save configuration successfully", configuration.getId(),
                is(notNullValue()));

        PasswordConfiguration reload = passwordConfigurationPersistence
                .reload(configuration.getId());
        assertThat("reload the same configuration successfully",
                reload.getId() == configuration.getId(), is(true));
    }
}
