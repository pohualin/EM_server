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
import com.emmisolutions.emmimanager.model.configuration.EmailRestrictConfiguration;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientRestrictConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.EmailRestrictConfigurationPersistence;

/**
 * Integration test for EmailRestrictConfigurationPersistence
 */
public class EmailRestrictConfigurationPersistenceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    ClientRestrictConfigurationPersistence clientRestrictConfigurationPersistence;

    @Resource
    EmailRestrictConfigurationPersistence emailRestrictConfigurationPersistence;

    /**
     * Test list
     */
    @Test
    public void testList() {
        Client client = makeNewRandomClient();

        EmailRestrictConfiguration configuration = new EmailRestrictConfiguration();
        configuration.setClient(client);
        // configuration.setDescription(RandomStringUtils.randomAlphabetic(255));
        configuration.setEmailEnding("emmisolutions.com");
        configuration = emailRestrictConfigurationPersistence
                .saveOrUpdate(configuration);

        EmailRestrictConfiguration configurationA = new EmailRestrictConfiguration();
        configurationA.setClient(client);
        configurationA.setDescription(RandomStringUtils.randomAlphabetic(255));
        configurationA.setEmailEnding("emmisolutions.net");
        configurationA = emailRestrictConfigurationPersistence
                .saveOrUpdate(configuration);

        Page<EmailRestrictConfiguration> listOfEmailConfig = emailRestrictConfigurationPersistence
                .list(new PageRequest(0, 10), client.getId());

        assertThat("should contain configuration",
                listOfEmailConfig.getContent(), hasItem(configuration));
        assertThat("should contain configurationA",
                listOfEmailConfig.getContent(), hasItem(configurationA));

        Page<EmailRestrictConfiguration> listOfEmailConfigA = emailRestrictConfigurationPersistence
                .list(null, client.getId());

        assertThat("should contain configuration",
                listOfEmailConfigA.getContent(), hasItem(configuration));
        assertThat("should contain configurationA",
                listOfEmailConfigA.getContent(), hasItem(configurationA));

        Page<EmailRestrictConfiguration> listOfEmailConfigB = emailRestrictConfigurationPersistence
                .list(new PageRequest(0, 10), null);

        assertThat("should contain configuration",
                listOfEmailConfigB.getContent(), hasItem(configuration));
        assertThat("should contain configurationA",
                listOfEmailConfigB.getContent(), hasItem(configurationA));
    }

    /**
     * Test save, reload, update and delete
     */
    @Test
    public void testSave() {
        Client client = makeNewRandomClient();

        EmailRestrictConfiguration configuration = new EmailRestrictConfiguration();
        configuration.setClient(client);
        configuration.setDescription(RandomStringUtils.randomAlphabetic(255));
        configuration.setEmailEnding("emmisolutions.com");
        configuration = emailRestrictConfigurationPersistence
                .saveOrUpdate(configuration);

        assertThat("EmailRestrictConfiguration should be saved.",
                configuration.getId(), is(notNullValue()));

        assertThat(
                "EmailRestrictConfiguration should be saved with email range start.",
                configuration.getEmailEnding(), is("emmisolutions.com"));

        EmailRestrictConfiguration reload = emailRestrictConfigurationPersistence
                .reload(configuration.getId());
        assertThat("should reload the same configuration",
                configuration.getId() == reload.getId(), is(true));

        reload.setEmailEnding("emmisolutions.net");
        EmailRestrictConfiguration update = emailRestrictConfigurationPersistence
                .saveOrUpdate(reload);
        assertThat("should update the email config start",
                update.getEmailEnding(), is("emmisolutions.net"));

        emailRestrictConfigurationPersistence.delete(configuration.getId());
        EmailRestrictConfiguration reloadAfterDelete = emailRestrictConfigurationPersistence
                .reload(configuration.getId());
        assertThat("should reload the same configuration",
                configuration.getId() == reload.getId(), is(true));
    }
}
