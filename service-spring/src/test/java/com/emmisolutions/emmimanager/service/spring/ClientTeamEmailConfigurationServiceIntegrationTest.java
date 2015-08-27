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
     * Test a null argument in saveOrUpdate()
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeSaveOrUpdateNull() {
        clientTeamEmailConfigurationService.saveOrUpdate(null);
    }

    /**
     * Test an empty ClientTeamEmailConfiguration in saveOrUpdate()
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeSaveOrUpdateEmptyTeam() {
        clientTeamEmailConfigurationService.saveOrUpdate(new ClientTeamEmailConfiguration());
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeSaveOrUpdateNullReloadTeam() {
        ClientTeamEmailConfiguration clientTeamEmailConfiguration = new ClientTeamEmailConfiguration();
        clientTeamEmailConfiguration.setTeam(new Team(new Long(69)));

        clientTeamEmailConfigurationService.saveOrUpdate(clientTeamEmailConfiguration);
    }

    /**
     * Test a null argument in findByTeam(). Should throw an exception.
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeFindByTeamNullTeam() {
        clientTeamEmailConfigurationService.findByTeam(null);
    }

    /**
     * Test an empty Team in findByTeam(). Should throw an exception.
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeFindByTeamNullTeamId() {
        clientTeamEmailConfigurationService.findByTeam(new Team());
    }

    /**
     * Test sending an invalid team to findByTeam(). Should throw an exception.
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testFindByTeamUnknownTeam() {
        clientTeamEmailConfigurationService.findByTeam(new Team(new Long(69)));
    }

    @Test
    public void testFindByTeamAndUpdate() {
        boolean reminderTwoDays = false;

        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);

        DefaultClientTeamEmailConfiguration defaultClientTeamEmailConfiguration = defaultClientTeamEmailConfigurationPersistence.find();
        ClientTeamEmailConfiguration clientTeamEmailConfiguration = clientTeamEmailConfigurationService.findByTeam(team);

        // Currently no clientTeamEmailConfiguration has been persisted; results should match defaults.
        assertThat("collectEmail should match default value.", clientTeamEmailConfiguration.getCollectEmail(), is(defaultClientTeamEmailConfiguration.getCollectEmail()));
        assertThat("requireEmail should match default value.", clientTeamEmailConfiguration.getRequireEmail(), is(defaultClientTeamEmailConfiguration.getRequireEmail()));
        assertThat("reminderTwoDays should match default value.", clientTeamEmailConfiguration.getReminderTwoDays(), is(defaultClientTeamEmailConfiguration.getReminderTwoDays()));
        assertThat("reminderFourDays should match default value.", clientTeamEmailConfiguration.getReminderFourDays(), is(defaultClientTeamEmailConfiguration.getReminderFourDays()));
        assertThat("reminderSixDays should match default value.", clientTeamEmailConfiguration.getReminderSixDays(), is(defaultClientTeamEmailConfiguration.getReminderSixDays()));
        assertThat("reminderEightDays should match default value.", clientTeamEmailConfiguration.getReminderEightDays(), is(defaultClientTeamEmailConfiguration.getReminderEightDays()));
        assertThat("reminderArticles should match default value.", clientTeamEmailConfiguration.getReminderArticles(), is(defaultClientTeamEmailConfiguration.getReminderArticles()));

        // Change settings, save, search again and verify changes persisted.
        clientTeamEmailConfiguration.setReminderTwoDays(reminderTwoDays);

        clientTeamEmailConfigurationService.saveOrUpdate(clientTeamEmailConfiguration);

        ClientTeamEmailConfiguration savedClientTeamEmailConfiguration = clientTeamEmailConfigurationService.findByTeam(clientTeamEmailConfiguration.getTeam());

        assertThat("reminderTwoDays should be false.", savedClientTeamEmailConfiguration.getReminderTwoDays(), is(reminderTwoDays));
    }

}
