package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientTeamSelfRegConfigurationService;
import com.emmisolutions.emmimanager.service.PatientSelfRegConfigurationService;
import com.emmisolutions.emmimanager.service.TeamService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

/**
 * An integration test for PatientSelfRegConfigurationServiceImpl
 */
public class PatientSelfRegConfigurationServiceIntegrationTest extends
        BaseIntegrationTest {
    @Resource
    ClientTeamSelfRegConfigurationService clientTeamSelfRegConfigurationService;

    @Resource
    TeamService teamService;

    @Resource
    PatientSelfRegConfigurationService patientSelfRegConfigurationService;

    /**
     * Test CRUD
     */
    @Test
    public void testcreateReloadUpdate() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);
        ClientTeamSelfRegConfiguration selfRegConfiguration = new ClientTeamSelfRegConfiguration();
        selfRegConfiguration.setTeam(team);
        selfRegConfiguration.setCode(RandomStringUtils.randomAlphanumeric(9));
        ClientTeamSelfRegConfiguration selfRegConfigurationCreated = clientTeamSelfRegConfigurationService.create(selfRegConfiguration);
        ClientTeamSelfRegConfiguration foundSelfRegConfiguration = clientTeamSelfRegConfigurationService.findByTeam(team);
        assertThat("the created self reg config is found:", foundSelfRegConfiguration, is(notNullValue()));

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
        patientSelfRegConfig.setTeam(team);

        PatientSelfRegConfig created = patientSelfRegConfigurationService.create(patientSelfRegConfig);
        assertThat("patient self registration configuration was created:", created.getId(), is(notNullValue()));
    }

    @Test
    public void testcreateReloadUpdateDefaultFalse() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);
        ClientTeamSelfRegConfiguration selfRegConfiguration = new ClientTeamSelfRegConfiguration();
        selfRegConfiguration.setTeam(team);
        selfRegConfiguration.setCode(RandomStringUtils.randomAlphanumeric(9));
        ClientTeamSelfRegConfiguration selfRegConfigurationCreated = clientTeamSelfRegConfigurationService.create(selfRegConfiguration);
        ClientTeamSelfRegConfiguration foundSelfRegConfiguration = clientTeamSelfRegConfigurationService.findByTeam(team);
        assertThat("the created self reg config is found:", foundSelfRegConfiguration.getId(), is(notNullValue()));

        PatientSelfRegConfig patientSelfRegConfig = new PatientSelfRegConfig();

        patientSelfRegConfig.setExposeDateOfBirth(true);
        patientSelfRegConfig.setExposeEmail(true);
        patientSelfRegConfig.setExposeId(true);
        patientSelfRegConfig.setExposeName(true);
        patientSelfRegConfig.setExposePhone(true);

        patientSelfRegConfig.setIdLabelType(PatientIdLabelType.OTHER_ID_LABEL);
        patientSelfRegConfig.setPatientIdLabelEnglish("label for id in english");
        patientSelfRegConfig.setPatientIdLabelSpanish("label for id in spanish");
        patientSelfRegConfig.setTeam(team);

        PatientSelfRegConfig created = patientSelfRegConfigurationService.create(patientSelfRegConfig);
        assertThat("patient self registration configuration was created:", created.getId(), is(notNullValue()));
        assertFalse(created.isRequirePhone());
        assertFalse(created.isRequireEmail());
        assertFalse(created.isRequireDateOfBirth());
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
    public void testEmptyLabelForOtherPatientIdLabelType() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);
        ClientTeamSelfRegConfiguration selfRegConfiguration = new ClientTeamSelfRegConfiguration();
        selfRegConfiguration.setTeam(team);
        selfRegConfiguration.setCode(RandomStringUtils.randomAlphanumeric(9));
        ClientTeamSelfRegConfiguration selfRegConfigurationCreated = clientTeamSelfRegConfigurationService.create(selfRegConfiguration);
        ClientTeamSelfRegConfiguration foundSelfRegConfiguration = clientTeamSelfRegConfigurationService.findByTeam(team);
        assertThat("the created self reg config is found:", foundSelfRegConfiguration.getId(), is(notNullValue()));

        PatientSelfRegConfig patientSelfRegConfig = new PatientSelfRegConfig();

        patientSelfRegConfig.setIdLabelType(PatientIdLabelType.OTHER_ID_LABEL);
        patientSelfRegConfig.setTeam(team);
        PatientSelfRegConfig created = patientSelfRegConfigurationService.create(patientSelfRegConfig);
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testBadSave(){
        PatientSelfRegConfig created = patientSelfRegConfigurationService.create(null);
    }
}
