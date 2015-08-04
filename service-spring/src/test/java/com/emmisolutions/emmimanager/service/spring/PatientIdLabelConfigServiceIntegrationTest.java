package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.PatientIdLabelConfigService;
import com.emmisolutions.emmimanager.service.PatientSelfRegConfigurationService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

/**
 * An integration test for PatientSelfRegConfigurationServiceImpl
 */
public class PatientIdLabelConfigServiceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    PatientIdLabelConfigService patientIdLabelConfigService;

    @Resource
    PatientSelfRegConfigurationService patientSelfRegConfigurationService;

    @Test
    public void testSaveLabels() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);
        PatientSelfRegConfig patientSelfRegConfig = new PatientSelfRegConfig();
        patientSelfRegConfig.setTeam(team);
        PatientSelfRegConfig patientSelfRegConfigCreated = patientSelfRegConfigurationService.create(patientSelfRegConfig);
        assertThat("patient self registration configuration was created:", patientSelfRegConfigCreated.getId(), is(notNullValue()));

        Language english = new Language();
        english.setLanguageTag("en");

        PatientIdLabelConfig patientIdLabelConfig = new PatientIdLabelConfig();
        patientIdLabelConfig.setLanguage(english);
        patientIdLabelConfig.setValue(RandomStringUtils.randomAlphabetic(10));
        patientIdLabelConfig.setPatientSelfRegConfig(patientSelfRegConfigCreated);
        patientIdLabelConfig.setIdLabelType(new PatientIdLabelType(1l));
        PatientIdLabelConfig savedPatientIdLabelConfig = patientIdLabelConfigService.create(patientIdLabelConfig);
        assertThat("PatientIdLabelConfig was saved for the given patient self reg config:", savedPatientIdLabelConfig, is(patientIdLabelConfigService.reload(savedPatientIdLabelConfig)));

        savedPatientIdLabelConfig.setValue("update patient id label value");
        PatientIdLabelConfig updatedPatientIdLabelConfig = patientIdLabelConfigService.update(savedPatientIdLabelConfig);
        assertThat("PatientIdLabelConfig was updated for the given patient self reg config:", updatedPatientIdLabelConfig, is(patientIdLabelConfigService.reload(updatedPatientIdLabelConfig)));

        PatientIdLabelConfigSearchFilter patientIdLabelConfigSearchFilter = new PatientIdLabelConfigSearchFilter(patientSelfRegConfigCreated);
        Page<PatientIdLabelConfig> patientIdLabelConfigs = patientIdLabelConfigService.list(null, patientIdLabelConfigSearchFilter);

        assertFalse("configPage found is not null:", patientIdLabelConfigs.getContent().isEmpty());
        assertThat("configPage found is not null:", patientIdLabelConfigs.getContent().iterator().next().getPatientSelfRegConfig().getId(), is(patientSelfRegConfigCreated.getId()));

        Page<PatientIdLabelConfig> page = patientIdLabelConfigService.list(new PageRequest(0, 1, Sort.Direction.ASC, "id"), patientIdLabelConfigSearchFilter);
        assertFalse("configPage found is not null:", page.getContent().isEmpty());

    }

    @Test
    public void testUnsavedReload() {
        PatientIdLabelConfig patientIdLabelConfig = new PatientIdLabelConfig();
        PatientIdLabelConfig reloadedPatientIdLabelConfig = patientIdLabelConfigService.reload(patientIdLabelConfig);
        assertThat("reloaded is null", reloadedPatientIdLabelConfig, is(nullValue()));

    }

    @Test
    public void testNullReload() {
        PatientIdLabelConfig reloadedPatientIdLabelConfig = patientIdLabelConfigService.reload(null);
        assertThat("reloaded is null", reloadedPatientIdLabelConfig, is(nullValue()));

    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testBadSaveIdLabel() {
        patientIdLabelConfigService.create(null);
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testBadUpdateIdLabel() {
        patientIdLabelConfigService.update(null);
    }
}
