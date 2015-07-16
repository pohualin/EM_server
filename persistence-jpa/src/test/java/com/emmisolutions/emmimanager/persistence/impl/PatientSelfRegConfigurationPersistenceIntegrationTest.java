package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientTeamSelfRegConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.PatientSelfRegConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.persistence.repo.InfoHeaderConfigRepository;
import com.emmisolutions.emmimanager.persistence.repo.LanguageRepository;
import com.emmisolutions.emmimanager.persistence.repo.PatientIdLabelConfigRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

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

    @Resource
    LanguageRepository languageRepository;

    @Resource
    InfoHeaderConfigRepository infoHeaderConfigRepository;

    @Resource
    PatientIdLabelConfigRepository patientIdLabelConfigRepository;

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

        patientSelfRegConfig.setTeam(teamPersistence.reload(team));
        PatientSelfRegConfig saved = patientSelfRegConfigurationPersistence.save(patientSelfRegConfig);
        assertThat("patient self registration configuration was reloaded:", saved, is(patientSelfRegConfigurationPersistence.reload(saved.getId())));
    }

    @Test
    public void testSaveReloadUpdateDefaultFalse() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);
        PatientSelfRegConfig patientSelfRegConfig = new PatientSelfRegConfig();

        patientSelfRegConfig.setExposeDateOfBirth(true);
        patientSelfRegConfig.setExposeEmail(true);
        patientSelfRegConfig.setExposeId(true);
        patientSelfRegConfig.setExposeName(true);
        patientSelfRegConfig.setExposePhone(true);
        patientSelfRegConfig.setTeam(teamPersistence.reload(team));
        PatientSelfRegConfig saved = patientSelfRegConfigurationPersistence.save(patientSelfRegConfig);

        assertThat("patient self registration configuration was saved:", saved, is(patientSelfRegConfigurationPersistence.findByTeamId(teamPersistence.reload(team).getId())));
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


    @Test
    public void testSaveLabels() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);
        PatientSelfRegConfig patientSelfRegConfig = new PatientSelfRegConfig();
        patientSelfRegConfig.setTeam(team);
        PatientSelfRegConfig created = patientSelfRegConfigurationPersistence.save(patientSelfRegConfig);
        assertThat("patient self registration configuration was created:", created.getId(), is(notNullValue()));

        Language english = languageRepository.findOne(1l);

        InfoHeaderConfig infoHeaderConfig = new InfoHeaderConfig();
        infoHeaderConfig.setPatientSelfRegConfig(created);
        infoHeaderConfig.setLanguage(english);
        infoHeaderConfig.setValue(RandomStringUtils.randomAlphabetic(10));
        InfoHeaderConfig saved = infoHeaderConfigRepository.save(infoHeaderConfig);

        assertThat("InfoHeaderConfig was saved for the given patient self reg config:", saved.getId(), is(notNullValue()));

        PatientIdLabelConfig patientIdLabelConfig = new PatientIdLabelConfig();
        patientIdLabelConfig.setLanguage(english);
        patientIdLabelConfig.setValue(RandomStringUtils.randomAlphabetic(10));
        patientIdLabelConfig.setPatientSelfRegConfig(created);
        patientIdLabelConfig.setIdLabelType(PatientIdLabelType.PATIENT_SELF_REG_LABEL_MEMBER_ID);
        PatientIdLabelConfig savedPatientIdLabelConfig = patientIdLabelConfigRepository.save(patientIdLabelConfig);
        assertThat("PatientIdLabelConfig was saved for the given patient self reg config:", savedPatientIdLabelConfig.getId(), is(notNullValue()));
    }
}