package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientTeamEmailConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.DefaultClientTeamEmailConfigurationPersistence;
import org.junit.Test;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Integration test for EmailRestrictConfigurationPersistence
 */
public class ClientTeamEmailConfigurationPersistenceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    ClientTeamEmailConfigurationPersistence clientTeamEmailConfigurationPersistence;
    
    @Resource
    DefaultClientTeamEmailConfigurationPersistence defaultClientTeamEmailConfigurationPersistence;

    @Test
    public void testCreateReloadUpdate() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);

        DefaultClientTeamEmailConfiguration defaultClientTeamEmailConfiguration = defaultClientTeamEmailConfigurationPersistence.find();

        ClientTeamEmailConfiguration clientTeamEmailConfiguration = new ClientTeamEmailConfiguration();
        clientTeamEmailConfiguration.setTeam(team);
        clientTeamEmailConfiguration.setCollectEmail(defaultClientTeamEmailConfiguration.getCollectEmail());
        clientTeamEmailConfiguration.setRequireEmail(defaultClientTeamEmailConfiguration.getRequireEmail());
        clientTeamEmailConfiguration.setReminderTwoDays(defaultClientTeamEmailConfiguration.getReminderTwoDays());
        clientTeamEmailConfiguration.setReminderFourDays(defaultClientTeamEmailConfiguration.getReminderFourDays());
        clientTeamEmailConfiguration.setReminderSixDays(defaultClientTeamEmailConfiguration.getReminderSixDays());
        clientTeamEmailConfiguration.setReminderEightDays(defaultClientTeamEmailConfiguration.getReminderEightDays());
        clientTeamEmailConfiguration.setReminderArticles(defaultClientTeamEmailConfiguration.getReminderArticles());

        ClientTeamEmailConfiguration savedClientTeamEmailConfiguration = clientTeamEmailConfigurationPersistence.save(clientTeamEmailConfiguration);

        ClientTeamEmailConfiguration findClientTeamEmailConfiguration = clientTeamEmailConfigurationPersistence.find(team.getId());

        assertThat("should contain configuration", findClientTeamEmailConfiguration, is(savedClientTeamEmailConfiguration));

        ClientTeamEmailConfiguration reloadedClientTeamEmailConfiguration = clientTeamEmailConfigurationPersistence.reload(savedClientTeamEmailConfiguration.getId());

        assertThat("should reload the same configuration", savedClientTeamEmailConfiguration, is(reloadedClientTeamEmailConfiguration));
    }

}
