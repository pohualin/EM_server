package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientTeamSelfRegConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.PatientSelfRegConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import org.junit.Test;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Integration test for PatientSelfRegConfigurationPersistence
 */
public class PatientSelfRegConfigurationPersistenceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    ClientTeamSelfRegConfigurationPersistence clientTeamSelfRegConfigurationPersistence;

    @Resource
    TeamPersistence teamPersistence;

    @Resource
    PatientSelfRegConfigurationPersistence patientSelfRegConfigurationPersistence;

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

        PatientSelfRegConfig patientSelfRegConfig = new PatientSelfRegConfig();
        patientSelfRegConfig.setRequirePhone(true);
        patientSelfRegConfig.setRequireName(true);
        patientSelfRegConfig.setRequireDateOfBirth(false);
        patientSelfRegConfig.setRequireEmail(false);
        patientSelfRegConfig.setRequireId(true);

        patientSelfRegConfig.setExposeDateOfBirth(true);
        patientSelfRegConfig.setExposeEmail(true);
        patientSelfRegConfig.setExposeId(true);
        patientSelfRegConfig.setExposeName(true);
        patientSelfRegConfig.setExposePhone(true);

        patientSelfRegConfig.setIdLabelType(PatientIdLabelType.MEMBER_ID);
        patientSelfRegConfig.setSelfRegConfiguration(selfRegConfigurationSaved);

        PatientSelfRegConfig saved = patientSelfRegConfigurationPersistence.save(patientSelfRegConfig);
        assertThat("patient self registration configuration was saved:", saved, is(patientSelfRegConfigurationPersistence.reload(saved.getId())));
    }

    @Test
    public void testSaveReloadUpdateDefaultFalse() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);
        ClientTeamSelfRegConfiguration selfRegConfiguration = new ClientTeamSelfRegConfiguration();
        selfRegConfiguration.setTeam(team);
        selfRegConfiguration.setCode("CODE_FOR_TEAM_ONE");
        ClientTeamSelfRegConfiguration selfRegConfigurationSaved = clientTeamSelfRegConfigurationPersistence.save(selfRegConfiguration);
        ClientTeamSelfRegConfiguration foundSelfRegConfiguration = clientTeamSelfRegConfigurationPersistence.find(team.getId());
        assertThat("the saved self reg config is found:", foundSelfRegConfiguration, is(clientTeamSelfRegConfigurationPersistence.reload(selfRegConfigurationSaved.getId())));

        PatientSelfRegConfig patientSelfRegConfig = new PatientSelfRegConfig();

        patientSelfRegConfig.setExposeDateOfBirth(true);
        patientSelfRegConfig.setExposeEmail(true);
        patientSelfRegConfig.setExposeId(true);
        patientSelfRegConfig.setExposeName(true);
        patientSelfRegConfig.setExposePhone(true);

        patientSelfRegConfig.setIdLabelType(PatientIdLabelType.OTHER);
        patientSelfRegConfig.setPatientIdLabelEnglish("label for id in english");
        patientSelfRegConfig.setPatientIdLabelSpanish("label for id in spanish");
        patientSelfRegConfig.setSelfRegConfiguration(selfRegConfigurationSaved);

        PatientSelfRegConfig saved = patientSelfRegConfigurationPersistence.save(patientSelfRegConfig);
        assertThat("patient self registration configuration was saved:", saved, is(patientSelfRegConfigurationPersistence.reload(saved.getId())));
        assertFalse(saved.isRequirePhone());
        assertFalse(saved.isRequireEmail());
        assertFalse(saved.isRequireDateOfBirth());
        assertFalse(saved.isRequireName());
        assertFalse(saved.isRequireId());

        assertTrue(saved.isExposeDateOfBirth());
        assertTrue(saved.isExposeEmail());
        assertTrue(saved.isExposeId());
        assertTrue(saved.isExposeName());
        assertTrue(saved.isExposePhone());
    }
}
