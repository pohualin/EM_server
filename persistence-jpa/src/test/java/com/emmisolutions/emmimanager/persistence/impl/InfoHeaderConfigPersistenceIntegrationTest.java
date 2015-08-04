package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.InfoHeaderConfigPersistence;
import com.emmisolutions.emmimanager.persistence.PatientIdLabelConfigPersistence;
import com.emmisolutions.emmimanager.persistence.PatientSelfRegConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.repo.LanguageRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

/**
 * Integration test for InfoHeaderConfigPersistence
 */
public class InfoHeaderConfigPersistenceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    PatientSelfRegConfigurationPersistence patientSelfRegConfigurationPersistence;

    @Resource
    LanguageRepository languageRepository;

    @Resource
    InfoHeaderConfigPersistence infoHeaderConfigPersistence;

    @Resource
    PatientIdLabelConfigPersistence patientIdLabelConfigPersistence;

    @Test
    public void testSaveLabels() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);
        PatientSelfRegConfig patientSelfRegConfig = new PatientSelfRegConfig();
        patientSelfRegConfig.setTeam(team);
        PatientSelfRegConfig patientSelfRegConfigCreated = patientSelfRegConfigurationPersistence.save(patientSelfRegConfig);
        assertThat("patient self registration configuration was created:", patientSelfRegConfigCreated.getId(), is(notNullValue()));

        Language english = languageRepository.findOne(1l);

        InfoHeaderConfig infoHeaderConfig = new InfoHeaderConfig();
        infoHeaderConfig.setPatientSelfRegConfig(patientSelfRegConfigCreated);
        infoHeaderConfig.setLanguage(english);
        infoHeaderConfig.setValue(RandomStringUtils.randomAlphabetic(10));
        InfoHeaderConfig saved = infoHeaderConfigPersistence.save(infoHeaderConfig);

        assertThat("InfoHeaderConfig was saved for the given patient self reg config:", saved.getId(), is(notNullValue()));
        assertThat("InfoHeaderConfig was saved for the given patient self reg config:", saved, is(infoHeaderConfigPersistence.reload(saved)));

        InfoHeaderConfigSearchFilter infoHeaderConfigSearchFilter = new InfoHeaderConfigSearchFilter(patientSelfRegConfigCreated);
        Page<InfoHeaderConfig> configPage = infoHeaderConfigPersistence.list(null, infoHeaderConfigSearchFilter);
        assertFalse("configPage found is not null:", configPage.getContent().isEmpty());
        assertThat("configPage found is not null:", configPage.getContent().iterator().next().getPatientSelfRegConfig().getId(), is(notNullValue()));

        Page<InfoHeaderConfig> configPageOnePerPage = infoHeaderConfigPersistence.list(new PageRequest(0, 1, Sort.Direction.ASC, "id"), infoHeaderConfigSearchFilter);
        assertFalse("configPage found is not null:", configPageOnePerPage.getContent().isEmpty());

        PatientIdLabelConfig patientIdLabelConfig = new PatientIdLabelConfig();
        patientIdLabelConfig.setLanguage(english);
        patientIdLabelConfig.setValue(RandomStringUtils.randomAlphabetic(10));
        patientIdLabelConfig.setPatientSelfRegConfig(patientSelfRegConfigCreated);
//        patientIdLabelConfig.setIdLabelType(PatientIdLabelType.PATIENT_SELF_REG_LABEL_MEMBER_ID);
        PatientIdLabelConfig savedPatientIdLabelConfig = patientIdLabelConfigPersistence.save(patientIdLabelConfig);
        assertThat("PatientIdLabelConfig was saved for the given patient self reg config:", savedPatientIdLabelConfig, is(patientIdLabelConfigPersistence.reload(savedPatientIdLabelConfig)));

        PatientIdLabelConfigSearchFilter patientIdLabelConfigSearchFilter = new PatientIdLabelConfigSearchFilter(patientSelfRegConfigCreated);
        Page<PatientIdLabelConfig> patientIdLabelConfigs = patientIdLabelConfigPersistence.list(null, patientIdLabelConfigSearchFilter);

        assertFalse("configPage found is not null:", patientIdLabelConfigs.getContent().isEmpty());
        assertThat("configPage found is not null:", patientIdLabelConfigs.getContent().iterator().next().getPatientSelfRegConfig().getId(), is(patientSelfRegConfigCreated.getId()));

        Page<PatientIdLabelConfig> page = patientIdLabelConfigPersistence.list(new PageRequest(0, 1, Sort.Direction.ASC, "id"), patientIdLabelConfigSearchFilter);
        assertFalse("configPage found is not null:", page.getContent().isEmpty());

    }

    @Test
    public void testUnsavedReload() {
        PatientIdLabelConfig patientIdLabelConfig = new PatientIdLabelConfig();
        PatientIdLabelConfig reloadedPatientIdLabelConfig = patientIdLabelConfigPersistence.reload(patientIdLabelConfig);
        assertThat("reloaded is null", reloadedPatientIdLabelConfig, is(nullValue()));

        InfoHeaderConfig infoHeaderConfig = new InfoHeaderConfig();
        InfoHeaderConfig reloadedInfoHeaderConfig = infoHeaderConfigPersistence.reload(infoHeaderConfig);
        assertThat("reloaded is null", reloadedInfoHeaderConfig, is(nullValue()));
    }

    @Test
    public void testNullReload() {
        PatientIdLabelConfig reloadedPatientIdLabelConfig = patientIdLabelConfigPersistence.reload(null);
        assertThat("reloaded is null", reloadedPatientIdLabelConfig, is(nullValue()));

        InfoHeaderConfig reloadedInfoHeaderConfig = infoHeaderConfigPersistence.reload(null);
        assertThat("reloaded is null", reloadedInfoHeaderConfig, is(nullValue()));
    }
}