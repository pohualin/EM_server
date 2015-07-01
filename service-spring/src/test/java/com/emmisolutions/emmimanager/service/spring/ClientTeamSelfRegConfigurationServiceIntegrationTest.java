package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientTeamSelfRegConfiguration;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientTeamSelfRegConfigurationService;
import com.emmisolutions.emmimanager.service.TeamService;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * An integration test for ClientTeamSelfRegConfigurationServiceImpl
 */
public class ClientTeamSelfRegConfigurationServiceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    ClientTeamSelfRegConfigurationService clientTeamSelfRegConfigurationService;

    @Resource
    TeamService teamService;

    /**
     * Test CRUD
     */
    @Test
    public void testCreateReloadUpdate() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);
        ClientTeamSelfRegConfiguration selfRegConfiguration = new ClientTeamSelfRegConfiguration();
        selfRegConfiguration.setTeam(team);
        selfRegConfiguration.setCode("newCodeJHGJHG");
        ClientTeamSelfRegConfiguration selfRegConfigurationSaved = clientTeamSelfRegConfigurationService.create(selfRegConfiguration);
        ClientTeamSelfRegConfiguration foundSelfRegConfiguration = clientTeamSelfRegConfigurationService.findByTeam(team);
        assertThat("the saved self reg config is found:", foundSelfRegConfiguration, is(selfRegConfigurationSaved));
    }

    /**
     * Test bad saveAndUpdate
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeSaveOrUpdateNull() {
        clientTeamSelfRegConfigurationService.create(null);
    }


    /**
     * Test bad saveAndUpdate
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeFindNull() {
        clientTeamSelfRegConfigurationService.findByTeam(new Team());
    }


    @Test(expected = ConstraintViolationException.class)
    public void testCreateReloadNoCode() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);
        ClientTeamSelfRegConfiguration selfRegConfiguration = new ClientTeamSelfRegConfiguration();
        selfRegConfiguration.setTeam(team);
        ClientTeamSelfRegConfiguration selfRegConfigurationSaved = clientTeamSelfRegConfigurationService.create(selfRegConfiguration);
        ClientTeamSelfRegConfiguration foundSelfRegConfiguration = clientTeamSelfRegConfigurationService.findByTeam(team);
        assertThat("the saved self reg config is found:", foundSelfRegConfiguration, is(selfRegConfigurationSaved));
    }

    @Test
    public void testCreateReloadUpdateCode() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);
        ClientTeamSelfRegConfiguration selfRegConfiguration = new ClientTeamSelfRegConfiguration();
        selfRegConfiguration.setTeam(team);
        selfRegConfiguration.setCode("CODE_FOR_TEAM_ONE");
        ClientTeamSelfRegConfiguration selfRegConfigurationSaved = clientTeamSelfRegConfigurationService.create(selfRegConfiguration);
        ClientTeamSelfRegConfiguration foundSelfRegConfiguration = clientTeamSelfRegConfigurationService.findByTeam(team);
        assertThat("the saved self reg config is found:", foundSelfRegConfiguration, is(selfRegConfigurationSaved));

        foundSelfRegConfiguration.setCode("UPDATED_CODE");
        ClientTeamSelfRegConfiguration selfRegConfigurationUpdated = clientTeamSelfRegConfigurationService.update(foundSelfRegConfiguration);
        assertThat("the saved self reg config is found:", selfRegConfigurationUpdated, is(foundSelfRegConfiguration));

        ClientTeamSelfRegConfiguration foundSelfRegConfiguration2 = clientTeamSelfRegConfigurationService.findByTeam(team);
        assertThat("the saved self reg config is found:", foundSelfRegConfiguration2, is(selfRegConfigurationUpdated));
    }

    @Test
    public void testResaveSameCodeName(){
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);
        ClientTeamSelfRegConfiguration selfRegConfiguration = new ClientTeamSelfRegConfiguration();
        selfRegConfiguration.setTeam(team);
        selfRegConfiguration.setCode("CODE");
        ClientTeamSelfRegConfiguration selfRegConfigurationSaved = clientTeamSelfRegConfigurationService.create(selfRegConfiguration);

        ClientTeamSelfRegConfiguration selfRegConfigurationSavedForUpdated = clientTeamSelfRegConfigurationService.update(selfRegConfigurationSaved);
        assertThat("is the same", selfRegConfigurationSaved.getId(), is(selfRegConfigurationSavedForUpdated.getId()));
    }
}
