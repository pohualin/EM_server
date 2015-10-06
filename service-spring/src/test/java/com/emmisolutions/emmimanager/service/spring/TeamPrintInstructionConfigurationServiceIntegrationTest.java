package com.emmisolutions.emmimanager.service.spring;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.configuration.team.TeamPrintInstructionConfiguration;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.TeamPrintInstructionConfigurationService;

/**
 * Test Service Implementation for TeamPrintInstructionConfiguration
 */
public class TeamPrintInstructionConfigurationServiceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    TeamPrintInstructionConfigurationService teamPrintInstructionConfigurationService;

    /**
     * Test get, save, reload, delete
     */
    @Test
    public void testSave() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);
        TeamPrintInstructionConfiguration configuration = teamPrintInstructionConfigurationService
                .get(team);
        assertThat(
                "should contain default team print instruction configuration",
                configuration.getDefaultTeamPrintInstructionConfiguration()
                        .isActive(), is(true));
        assertThat(
                "should contain team print instruction configuration with default value",
                configuration.getPatientUrlLabel(), is("startemmi.com"));

        configuration.setPatientUrlLabel("abc.com");
        configuration = teamPrintInstructionConfigurationService
                .create(configuration);

        assertThat(
                "should contain team print instruction configuration with new patient url",
                configuration.getPatientUrlLabel(), is("abc.com"));

        configuration.setPatientUrlLabel("startemmi.com");
        configuration = teamPrintInstructionConfigurationService
                .update(configuration);

        assertThat(
                "should contain team print instruction configuration with updated patient url",
                configuration.getPatientUrlLabel(), is("startemmi.com"));

        TeamPrintInstructionConfiguration reload = teamPrintInstructionConfigurationService
                .reload(new TeamPrintInstructionConfiguration(configuration
                        .getId()));
        TeamPrintInstructionConfiguration getInserted = teamPrintInstructionConfigurationService
                .get(team);
        assertThat("reload the same instance", reload.getId(),
                is(configuration.getId()));
        assertThat("reload and get the same instance", reload.getId(),
                is(getInserted.getId()));

        teamPrintInstructionConfigurationService.delete(reload);
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeDeleteWithNull() {
        teamPrintInstructionConfigurationService.delete(null);
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeDeleteWithNullId() {
        teamPrintInstructionConfigurationService
                .delete(new TeamPrintInstructionConfiguration());
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeReloadWithNull() {
        teamPrintInstructionConfigurationService.reload(null);
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeReloadWithNullId() {
        teamPrintInstructionConfigurationService
                .reload(new TeamPrintInstructionConfiguration());
    }

}
