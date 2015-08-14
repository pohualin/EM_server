package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientTeamPhoneConfiguration;
import com.emmisolutions.emmimanager.model.ClientTeamSelfRegConfiguration;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamPhoneConfiguration;
import com.emmisolutions.emmimanager.persistence.*;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Integration test for PhoneRestrictConfigurationPersistence
 */
public class ClientTeamSelfRegConfigurationPersistenceIntegrationTest extends
        BaseIntegrationTest {   
    
    @Resource
    ClientTeamSelfRegConfigurationPersistence clientTeamSelfRegConfigurationPersistence;

    @Resource
    TeamPersistence teamPersistence;

    /**
     * Test CRUD
     */
    @Test
    public void testSaveReloadUpdate() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);
        ClientTeamSelfRegConfiguration selfRegConfiguration = new ClientTeamSelfRegConfiguration();
        selfRegConfiguration.setTeam(team);
        selfRegConfiguration.setCode("CODE_FOR_TEAM_ONE");
        ClientTeamSelfRegConfiguration selfRegConfigurationSaved = clientTeamSelfRegConfigurationPersistence.save(selfRegConfiguration);
        ClientTeamSelfRegConfiguration foundSelfRegConfiguration = clientTeamSelfRegConfigurationPersistence.find(team.getId());
        assertThat("the saved self reg config is found:", foundSelfRegConfiguration, is(clientTeamSelfRegConfigurationPersistence.reload(selfRegConfigurationSaved.getId())));
    }

    @Test(expected = ConstraintViolationException.class)
    public void testSaveReloadNoCode() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);
        ClientTeamSelfRegConfiguration selfRegConfiguration = new ClientTeamSelfRegConfiguration();
        selfRegConfiguration.setTeam(team);
        ClientTeamSelfRegConfiguration selfRegConfigurationSaved = clientTeamSelfRegConfigurationPersistence.save(selfRegConfiguration);
    }

    @Test
    public void testSaveReloadUpdateCode() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);
        ClientTeamSelfRegConfiguration selfRegConfiguration = new ClientTeamSelfRegConfiguration();
        selfRegConfiguration.setTeam(team);
        selfRegConfiguration.setCode("CODE_FOR_TEAM_ONE");
        ClientTeamSelfRegConfiguration selfRegConfigurationSaved = clientTeamSelfRegConfigurationPersistence.save(selfRegConfiguration);
        ClientTeamSelfRegConfiguration foundSelfRegConfiguration = clientTeamSelfRegConfigurationPersistence.findByCode(selfRegConfigurationSaved.getCode());
        assertThat("the saved self reg config is found by code:", foundSelfRegConfiguration, is(selfRegConfigurationSaved));

        foundSelfRegConfiguration.setCode("UPDATED_CODE");
        ClientTeamSelfRegConfiguration selfRegConfigurationUpdated = clientTeamSelfRegConfigurationPersistence.save(foundSelfRegConfiguration);
        assertThat("the updated self reg config is found:", selfRegConfigurationUpdated, is(foundSelfRegConfiguration));

        ClientTeamSelfRegConfiguration foundSelfRegConfiguration2 = clientTeamSelfRegConfigurationPersistence.find(team.getId());
        assertThat("the saved self reg config is found:", foundSelfRegConfiguration2, is(selfRegConfigurationUpdated));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testDuplicateCodeName(){
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);
        ClientTeamSelfRegConfiguration selfRegConfiguration = new ClientTeamSelfRegConfiguration();
        selfRegConfiguration.setTeam(team);
        selfRegConfiguration.setCode("CODE_FOR_TEAM_ONE");
        ClientTeamSelfRegConfiguration selfRegConfigurationSaved = clientTeamSelfRegConfigurationPersistence.save(selfRegConfiguration);

        Team teamTwo = makeNewRandomTeam(client);
        ClientTeamSelfRegConfiguration selfRegConfigurationForTeamTwo = new ClientTeamSelfRegConfiguration();
        selfRegConfigurationForTeamTwo.setTeam(teamTwo);
        selfRegConfigurationForTeamTwo.setCode("CODE_FOR_TEAM_ONE");
        ClientTeamSelfRegConfiguration selfRegConfigurationSavedForTeamTwo = clientTeamSelfRegConfigurationPersistence.save(selfRegConfigurationForTeamTwo);
    }

    @Test
    public void testGetSelfRegConfigByCode(){
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);
        ClientTeamSelfRegConfiguration selfRegConfiguration = new ClientTeamSelfRegConfiguration();
        selfRegConfiguration.setTeam(team);
        selfRegConfiguration.setCode("codeToTestFindByCode");
        ClientTeamSelfRegConfiguration selfRegConfigurationSaved = clientTeamSelfRegConfigurationPersistence.save(selfRegConfiguration);
        ClientTeamSelfRegConfiguration foundSelfRegConfiguration = clientTeamSelfRegConfigurationPersistence.findByCode(selfRegConfiguration.getCode());
        assertThat("the saved self reg config is found by code:", foundSelfRegConfiguration, is(clientTeamSelfRegConfigurationPersistence.reload(selfRegConfigurationSaved.getId())));
    }
}
