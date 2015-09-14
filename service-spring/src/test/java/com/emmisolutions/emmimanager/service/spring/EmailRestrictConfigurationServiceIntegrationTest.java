package com.emmisolutions.emmimanager.service.spring;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.EmailRestrictConfiguration;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientRestrictConfigurationService;
import com.emmisolutions.emmimanager.service.EmailRestrictConfigurationService;

/**
 * An integration test for EmailRestrictConfigurationServiceImpl
 */
public class EmailRestrictConfigurationServiceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    ClientRestrictConfigurationService clientRestrictConfigurationService;

    @Resource
    EmailRestrictConfigurationService emailRestrictConfigurationService;

    public static final String EMAIL_ENDING_DUPLICATE = "duplicate.email.ending.emmisolutions.com";

    /**
     * Test CRUD
     */
    @Test
    public void testCreateReloadUpdateDelete() {
        Client client = makeNewRandomClient();

        EmailRestrictConfiguration emailConfig = new EmailRestrictConfiguration();
        emailConfig.setClient(client);
        emailConfig.setEmailEnding("emmisolutions.com");
        emailConfig.setDescription(RandomStringUtils.randomAlphabetic(255));

        assertFalse("should not have a duplicate email ending",
                emailRestrictConfigurationService.hasDuplicateEmailEnding(emailConfig));

        emailConfig = emailRestrictConfigurationService.create(emailConfig);

        assertThat("emailConfig saved with id", emailConfig.getId(),
                is(notNullValue()));

        EmailRestrictConfiguration reloadEmailConfig = emailRestrictConfigurationService
                .reload(new EmailRestrictConfiguration(emailConfig.getId()));

        assertThat("should reload the same email configuration",
                reloadEmailConfig.getId(), is(emailConfig.getId()));

        EmailRestrictConfiguration updateEmailConfig = reloadEmailConfig;
        updateEmailConfig.setEmailEnding("emmisolutions.net");
        updateEmailConfig = emailRestrictConfigurationService
                .update(updateEmailConfig);

        assertThat("should update the existing email configuration",
                updateEmailConfig.getId(), is(reloadEmailConfig.getId()));
        assertThat("should have the updated email ending",
                updateEmailConfig.getEmailEnding(), is("emmisolutions.net"));

        emailRestrictConfigurationService.delete(updateEmailConfig);
    }

    /**
     * Test getByClientRestrictConfiguration
     */
    @Test
    public void testGetByClientRestrictConfiguration() {
        Client client = makeNewRandomClient();

        EmailRestrictConfiguration emailConfig = new EmailRestrictConfiguration();
        emailConfig.setClient(client);
        emailConfig.setEmailEnding("emmisolutionsA.com");
        emailConfig.setDescription(RandomStringUtils.randomAlphabetic(255));
        emailConfig = emailRestrictConfigurationService.create(emailConfig);

        EmailRestrictConfiguration emailConfigA = new EmailRestrictConfiguration();
        emailConfigA.setClient(client);
        emailConfigA.setEmailEnding("emmisolutionsB.com");
        emailConfigA.setDescription(RandomStringUtils.randomAlphabetic(255));
        emailConfigA = emailRestrictConfigurationService.create(emailConfigA);

        Page<EmailRestrictConfiguration> page = emailRestrictConfigurationService
                .getByClient(null, client);

        assertThat("return page should include email config",
                page.getContent(), hasItem(emailConfig));

        assertThat("return page should include another email config",
                page.getContent(), hasItem(emailConfigA));

    }

    /**
     * Test bad Delete
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeDeleteNull() {
        emailRestrictConfigurationService.delete(null);
    }

    /**
     * Test bad Delete
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeDeleteNullId() {
        emailRestrictConfigurationService.delete(new EmailRestrictConfiguration());
    }

    /**
     * Test bad Reload
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeReloadNull() {
        emailRestrictConfigurationService.reload(null);
    }

    /**
     * Test bad Reload
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeReloadNullId() {
        emailRestrictConfigurationService.reload(new EmailRestrictConfiguration());
    }

    /**
     * Test bad Get
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeGetNull() {
        emailRestrictConfigurationService.getByClient(null, null);
    }

    /**
     * Test bad Get
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeGetNullId() {
        emailRestrictConfigurationService.getByClient(null, new Client());
    }

    /**
     * Test bad Update
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeUpdateNull() {
        emailRestrictConfigurationService.update(null);
    }

    /**
     * Test bad Update
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeUpdateNullId() {
        emailRestrictConfigurationService.update(new EmailRestrictConfiguration());
    }

    /**
     * Test bad create
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeCreateNullConfig() {
        emailRestrictConfigurationService.create(null);
    }

    /**
     * Test hasDuplicateEmailEnding with null argument
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeHasDuplicateEmailEndingNullConfig() {
        emailRestrictConfigurationService.hasDuplicateEmailEnding(null);
    }

    /**
     * Test hasDuplicateEmailEnding with null Client
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeHasDuplicateEmailEndingNullClient() {
        emailRestrictConfigurationService.hasDuplicateEmailEnding(new EmailRestrictConfiguration());
    }

    /**
     * Test hasDuplicateEmailEnding
     */
    @Test
    public void testHasDuplicateEmailEnding() {
        Client client = makeNewRandomClient();

        EmailRestrictConfiguration emailConfig = new EmailRestrictConfiguration();
        emailConfig.setClient(client);
        emailConfig.setEmailEnding(EMAIL_ENDING_DUPLICATE);
        emailConfig.setDescription(RandomStringUtils.randomAlphabetic(255));

        assertFalse("Should not have a duplicate email", emailRestrictConfigurationService.hasDuplicateEmailEnding(emailConfig));

        emailConfig = emailRestrictConfigurationService.create(emailConfig);

        EmailRestrictConfiguration duplicate = new EmailRestrictConfiguration();
        duplicate.setClient(client);
        duplicate.setEmailEnding(EMAIL_ENDING_DUPLICATE);
        duplicate.setDescription(RandomStringUtils.randomAlphabetic(255));

        assertThat("Should have a duplicate entry", emailRestrictConfigurationService.hasDuplicateEmailEnding(duplicate), is(true));

    }
}
