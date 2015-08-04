package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.persistence.*;
import org.junit.Test;
import org.springframework.data.domain.Page;

import javax.annotation.Resource;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
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
    LanguagePersistence languagePersistence;

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
    public void testListLanguages() {
        Page<Language> languagePage = languagePersistence.list(null);
        assertFalse("languages exist:", languagePage.getContent().isEmpty());
    }

    @Test
    public void testGetAllPatientIdLabelTypes() {
        Collection<PatientIdLabelType> types = patientSelfRegConfigurationPersistence.getAllPatientIdLabelTypes();
        assertFalse("languages exist:", types.isEmpty());
        assertThat("There are currently 5 patient id types", types.size(), is(5));
    }

    @Test
    public void testTranslations() {
        Collection<PatientIdLabelType> types = patientSelfRegConfigurationPersistence.getAllPatientIdLabelTypes();
        PatientIdLabelType patientIdLabelType = types.iterator().next();
        Page<Strings> strings = patientSelfRegConfigurationPersistence.findByString(patientIdLabelType.getTypeKey(), null);
        assertFalse("translation is not null", strings.getContent().isEmpty());
    }

}