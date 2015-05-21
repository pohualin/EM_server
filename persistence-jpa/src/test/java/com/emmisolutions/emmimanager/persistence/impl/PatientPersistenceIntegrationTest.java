package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.PatientSearchFilter;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.PatientPersistence;
import org.junit.Test;
import org.springframework.data.domain.Page;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Persistence Integration Test for Patient
 */
public class PatientPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    PatientPersistence patientPersistence;

    @Resource
    ClientPersistence clientPersistence;

    @Test
    public void testPatientCreateAndReload() {
        Patient reloadedPatient = patientPersistence.reload(makeNewRandomPatient(null));
        assertThat("Patient was reloaded", reloadedPatient.getId(), is(notNullValue()));
    }

    @Test
    public void testList() {
        Patient patient = makeNewRandomPatient(null);
        PatientSearchFilter filter = new PatientSearchFilter(patient.getClient(), patient.getFirstName());
        Page<Patient> patientPage = patientPersistence.list(null, filter);
        assertThat("Page of Patients retrieved contains the searched item", patientPage, hasItem(patient));
    }
}
