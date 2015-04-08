package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.PatientPersistence;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.LocalDate;
import org.junit.Test;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
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
    public void testPatientCreate() {
        Patient patient = new Patient();
        patient.setFirstName(RandomStringUtils.randomAlphabetic(18));
        patient.setLastName(RandomStringUtils.randomAlphabetic(20));
        patient.setDateOfBirth(LocalDate.now());
        patient.setGender(Gender.Female);
        patient.setClient(clientPersistence.save(makeClient(RandomStringUtils.randomAlphabetic(15))));
        Patient savedPatient = patientPersistence.save(patient);
        assertThat("Patient was created", savedPatient.getId(), is(notNullValue()));

        Patient reloadedPatient = patientPersistence.reload(savedPatient);
        assertThat("Patient was reloaded", reloadedPatient.getId(), is(notNullValue()));

    }

    private Client makeClient(String clientName) {
        Client client = new Client();
        client.setType(new ClientType(1l));
        client.setContractStart(LocalDate.now());
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setName(clientName);
        client.setContractOwner(new UserAdmin(1l, 0));
        client.setSalesForceAccount(new SalesForce(RandomStringUtils.randomAlphanumeric(18)));
        return client;
    }
}
