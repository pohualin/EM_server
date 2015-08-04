package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.InfoHeaderConfigPersistence;
import com.emmisolutions.emmimanager.persistence.LanguagePersistence;
import com.emmisolutions.emmimanager.persistence.PatientSelfRegConfigurationPersistence;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
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
    LanguagePersistence languagePersistence;

    @Resource
    InfoHeaderConfigPersistence infoHeaderConfigPersistence;

    @Test
    public void testInfoHeaderConfigPersistenceImpl() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);
        PatientSelfRegConfig patientSelfRegConfig = new PatientSelfRegConfig();
        patientSelfRegConfig.setTeam(team);
        PatientSelfRegConfig patientSelfRegConfigCreated = patientSelfRegConfigurationPersistence.save(patientSelfRegConfig);
        assertThat("patient self registration configuration was created:", patientSelfRegConfigCreated.getId(), is(notNullValue()));

        Language english = languagePersistence.reload(new Language(1l));
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
    }

}