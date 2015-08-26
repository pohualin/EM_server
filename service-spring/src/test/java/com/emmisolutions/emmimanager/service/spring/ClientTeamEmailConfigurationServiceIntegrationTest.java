package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.persistence.DefaultClientTeamEmailConfigurationPersistence;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientTeamEmailConfigurationService;
import com.emmisolutions.emmimanager.service.TeamService;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * An integration test for ClientTeamEmailConfigurationServiceImpl
 */
public class ClientTeamEmailConfigurationServiceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    ClientTeamEmailConfigurationService clientTeamEmailConfigurationService;

    @Resource
    DefaultClientTeamEmailConfigurationPersistence defaultClientTeamEmailConfigurationPersistence;

    @Resource
    TeamService teamService;

    /**
     * Test CRUD
     */
    @Test
    public void testCreateReloadUpdate() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);

        DefaultClientTeamEmailConfiguration defaultClientTeamEmailConfiguration = defaultClientTeamEmailConfigurationPersistence.find();
        System.out.println(defaultClientTeamEmailConfiguration);

        ClientTeamEmailConfiguration clientTeamEmailConfiguration = new ClientTeamEmailConfiguration();
        clientTeamEmailConfiguration.setTeam(team);
        clientTeamEmailConfiguration.setCollectEmail(defaultClientTeamEmailConfiguration.getCollectEmail());
        clientTeamEmailConfiguration.setRequireEmail(defaultClientTeamEmailConfiguration.getRequireEmail());
        clientTeamEmailConfiguration.setReminderTwoDays(defaultClientTeamEmailConfiguration.getReminderTwoDays());
        clientTeamEmailConfiguration.setReminderFourDays(defaultClientTeamEmailConfiguration.getReminderFourDays());
        clientTeamEmailConfiguration.setReminderSixDays(defaultClientTeamEmailConfiguration.getReminderSixDays());
        clientTeamEmailConfiguration.setReminderEightDays(defaultClientTeamEmailConfiguration.getReminderEightDays());
        clientTeamEmailConfiguration.setReminderArticles(defaultClientTeamEmailConfiguration.getReminderArticles());

        ClientTeamEmailConfiguration savedClientTeamEmailConfiguration = clientTeamEmailConfigurationService.saveOrUpdate(clientTeamEmailConfiguration);

        ClientTeamEmailConfiguration findClientTeamEmailConfiguration = clientTeamEmailConfigurationService.findByTeam(team);

        assertThat("should contain configuration", findClientTeamEmailConfiguration, is(savedClientTeamEmailConfiguration));
    }

    /**
     * Test bad saveAndUpdate
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeSaveOrUpdateNull() {
        clientTeamEmailConfigurationService.saveOrUpdate(null);
    }

    /**
     * Test bad find
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeFindNull() {
        clientTeamEmailConfigurationService.findByTeam(new Team());
    }

}
