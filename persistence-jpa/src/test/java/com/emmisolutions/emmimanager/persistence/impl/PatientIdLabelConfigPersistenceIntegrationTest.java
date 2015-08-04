package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.persistence.*;
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
 * Integration test for PatientIdLabelConfigPersistence
 */
public class PatientIdLabelConfigPersistenceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    PatientSelfRegConfigurationPersistence patientSelfRegConfigurationPersistence;

    @Resource
    LanguagePersistence languagePersistence;

    @Resource
    InfoHeaderConfigPersistence infoHeaderConfigPersistence;

    @Resource
    PatientIdLabelConfigPersistence patientIdLabelConfigPersistence;

    @Test
    public void testSavePatientIdLabelConfigPersistence() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);
        PatientSelfRegConfig patientSelfRegConfig = makeNewRandomPatientSelfRegConfig(team);
        assertThat("patient self registration configuration was created:", patientSelfRegConfig.getId(), is(notNullValue()));

        Language english = languagePersistence.reload(new Language(1l));
        PatientIdLabelConfig patientIdLabelConfig = new PatientIdLabelConfig();
        patientIdLabelConfig.setLanguage(english);
        patientIdLabelConfig.setValue(RandomStringUtils.randomAlphabetic(10));
        patientIdLabelConfig.setPatientSelfRegConfig(patientSelfRegConfig);
        patientIdLabelConfig.setIdLabelType(new PatientIdLabelType(1l));
        PatientIdLabelConfig savedPatientIdLabelConfig = patientIdLabelConfigPersistence.save(patientIdLabelConfig);
        assertThat("PatientIdLabelConfig was saved for the given patient self reg config:", savedPatientIdLabelConfig, is(patientIdLabelConfigPersistence.reload(savedPatientIdLabelConfig)));

        PatientIdLabelConfigSearchFilter patientIdLabelConfigSearchFilter = new PatientIdLabelConfigSearchFilter(patientSelfRegConfig);
        Page<PatientIdLabelConfig> patientIdLabelConfigs = patientIdLabelConfigPersistence.list(null, patientIdLabelConfigSearchFilter);

        assertFalse("configPage found is not null:", patientIdLabelConfigs.getContent().isEmpty());
        assertThat("configPage found is not null:", patientIdLabelConfigs.getContent().iterator().next().getPatientSelfRegConfig().getId(), is(patientSelfRegConfig.getId()));

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