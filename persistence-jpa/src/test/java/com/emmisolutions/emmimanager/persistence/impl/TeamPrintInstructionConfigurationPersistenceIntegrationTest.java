package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.configuration.team.TeamPrintInstructionConfiguration;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.DefaultTeamPrintInstructionConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPrintInstructionConfigurationPersistence;

/**
 * Test TeamPrintInstructionConfigurationPersistence
 */
public class TeamPrintInstructionConfigurationPersistenceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    TeamPrintInstructionConfigurationPersistence teamPrintInstructionConfigurationPersistence;

    @Resource
    DefaultTeamPrintInstructionConfigurationPersistence defaultTeamPrintInstructionConfigurationPersistence;

    /**
     * Test negative reload
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testReload() {
        assertThat("bad reload",
                teamPrintInstructionConfigurationPersistence.reload(null),
                is(nullValue()));
    }

    /**
     * Test negative save
     */
    @Test(expected = ConstraintViolationException.class)
    public void testSaveWithoutDefaultTeamPrintInstructionConfiguration() {
        Client client = makeNewRandomClient();
        TeamPrintInstructionConfiguration configuration = new TeamPrintInstructionConfiguration();
        configuration.setTeam(makeNewRandomTeam(client));
        configuration = teamPrintInstructionConfigurationPersistence
                .saveOrUpdate(configuration);
        assertThat("save configuration successfully", configuration.getId(),
                is(notNullValue()));
    }

    /**
     * Test save, reload and delete
     */
    @Test
    public void testSave() {
        Client client = makeNewRandomClient();
        TeamPrintInstructionConfiguration configuration = new TeamPrintInstructionConfiguration();
        configuration.setTeam(makeNewRandomTeam(client));
        configuration
                .setDefaultTeamPrintInstructionConfiguration(defaultTeamPrintInstructionConfigurationPersistence
                        .findActive());
        populateFromDefault(configuration);
        configuration = teamPrintInstructionConfigurationPersistence
                .saveOrUpdate(configuration);
        assertThat("save configuration successfully", configuration.getId(),
                is(notNullValue()));

        TeamPrintInstructionConfiguration reload = teamPrintInstructionConfigurationPersistence
                .reload(configuration.getId());
        assertThat("reload configuration successfully", reload.getId(),
                is(notNullValue()));

        teamPrintInstructionConfigurationPersistence.delete(reload.getId());
        assertThat("reload configuration successfully",
                teamPrintInstructionConfigurationPersistence.reload(reload
                        .getId()), is(nullValue()));
    }

    /**
     * Test findByClient
     */
    @Test
    public void testFindByTeam() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);
        TeamPrintInstructionConfiguration configuration = new TeamPrintInstructionConfiguration();
        configuration.setTeam(team);
        configuration
                .setDefaultTeamPrintInstructionConfiguration(defaultTeamPrintInstructionConfigurationPersistence
                        .reload(1l));
        populateFromDefault(configuration);
        configuration = teamPrintInstructionConfigurationPersistence
                .saveOrUpdate(configuration);

        TeamPrintInstructionConfiguration findByTeam = teamPrintInstructionConfigurationPersistence
                .findByTeam(team);
        assertThat("should find one TeamPrintInstructionConfiguration by team",
                findByTeam.getId(), is(notNullValue()));
        assertThat(
                "should find one TeamPrintInstructionConfiguration with the passed in team",
                findByTeam.getTeam(), is(team));
    }

    private void populateFromDefault(
            TeamPrintInstructionConfiguration configuration) {
        configuration.setPatientUrlLabel(configuration
                .getDefaultTeamPrintInstructionConfiguration().getPatientUrlLabel());
    }
}
