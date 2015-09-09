package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.PatientSelfRegConfigurationService;
import com.emmisolutions.emmimanager.service.TeamService;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;

import javax.annotation.Resource;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

/**
 * An integration test for PatientSelfRegConfigurationServiceImpl
 */
public class PatientSelfRegConfigurationServiceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    TeamService teamService;

    @Resource
    PatientSelfRegConfigurationService patientSelfRegConfigurationService;

    /**
     * Test CRUD
     */
    @Test
    public void testCreateReloadUpdate() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);
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
        patientSelfRegConfig.setTeam(team);

        PatientSelfRegConfig created = patientSelfRegConfigurationService.create(patientSelfRegConfig);
        assertThat("patient self registration configuration was created:", created.getId(), is(notNullValue()));
    }

    @Test
    public void testCreateReloadUpdateDefaultFalse() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);

        PatientSelfRegConfig patientSelfRegConfig = new PatientSelfRegConfig();
        patientSelfRegConfig.setExposeDateOfBirth(true);
        patientSelfRegConfig.setExposeEmail(true);
        patientSelfRegConfig.setExposeId(true);
        patientSelfRegConfig.setExposeName(true);
        patientSelfRegConfig.setExposePhone(true);
        patientSelfRegConfig.setTeam(team);
        PatientSelfRegConfig created = patientSelfRegConfigurationService.create(patientSelfRegConfig);
        assertThat("patient self registration configuration was created:", created.getId(), is(notNullValue()));

        assertFalse(created.isRequirePhone());
        assertFalse(created.isRequireEmail());
        assertFalse(created.isRequireName());
        assertFalse(created.isRequireId());
        assertTrue(created.isExposeDateOfBirth());
        assertTrue(created.isExposeEmail());
        assertTrue(created.isExposeId());
        assertTrue(created.isExposeName());
        assertTrue(created.isExposePhone());
        PatientSelfRegConfig updated = patientSelfRegConfigurationService.update(created);
        assertThat("patient self registration configuration was updated:", updated, is(patientSelfRegConfigurationService.findByTeam(team)));
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testBadSave() {
        PatientSelfRegConfig created = patientSelfRegConfigurationService.create(null);
    }

    @Test
    public void testListLanguages() {
        Page<Language> languagePage = patientSelfRegConfigurationService.findAllAvailableLanguages(null);
        assertFalse("languages exist:", languagePage.getContent().isEmpty());
    }

    @Test
    public void testGetAllPatientIdLabelTypes() {
        Collection<PatientIdLabelType> types = patientSelfRegConfigurationService.getAllPatientIdLabelTypes();
        assertFalse("languages exist:", types.isEmpty());
        assertThat("There are currently 5 patient id types", types.size(), is(5));
    }

    @Test
    public void testTranslations() {
        Collection<PatientIdLabelType> types = patientSelfRegConfigurationService.getAllPatientIdLabelTypes();
        PatientIdLabelType patientIdLabelType = types.iterator().next();
        Page<Strings> strings = patientSelfRegConfigurationService.findByString(patientIdLabelType.getTypeKey(), null);
        assertFalse("translation is not null", strings.getContent().isEmpty());
    }

    @Test
    public void testBusinessRules() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);

        PatientSelfRegConfig patientSelfRegConfig = new PatientSelfRegConfig();
        patientSelfRegConfig.setTeam(team);
        patientSelfRegConfig.setRequireName(false);
        patientSelfRegConfig.setExposeEmail(false);
        patientSelfRegConfig.setExposeId(false);
        patientSelfRegConfig.setExposePhone(false);
        patientSelfRegConfig.setRequirePhone(true);
        patientSelfRegConfig.setRequireEmail(true);
        patientSelfRegConfig.setRequireId(true);
        patientSelfRegConfig.setExposeName(true);
        PatientSelfRegConfig created = patientSelfRegConfigurationService.create(patientSelfRegConfig);
        assertThat("patient self registration configuration was created:", created.getId(), is(notNullValue()));

        assertFalse(created.isRequirePhone());
        assertFalse(created.isRequireEmail());
        assertFalse(created.isRequireId());
        assertFalse(created.isExposeEmail());
        assertFalse(created.isExposeId());
        assertFalse(created.isExposePhone());
        assertFalse(created.isRequireName());
        assertTrue(created.isExposeName());
    }
}
