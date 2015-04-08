package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.PatientService;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration Test for PatientService
 */
public class PatientServiceIntegrationTest extends BaseIntegrationTest {

    @Resource
    PatientService patientService;

    @Resource
    ClientService clientService;

    @Test
    public void testPatientCreate() {
        Patient patient = new Patient();
        patient.setFirstName(RandomStringUtils.randomAlphabetic(18));
        patient.setLastName(RandomStringUtils.randomAlphabetic(20));
        patient.setDateOfBirth(LocalDate.now());
        patient.setClient(clientService.create(makeClient(RandomStringUtils.randomAlphabetic(15))));
        Patient savedPatient = patientService.create(patient);
        assertThat("Patient was created", savedPatient.getId(), is(notNullValue()));

        Patient reloadedPatient = patientService.reload(savedPatient);
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


    @Test
    public void testPatientUpdate() {
        Patient patient = new Patient();
        patient.setFirstName(RandomStringUtils.randomAlphabetic(18));
        patient.setLastName(RandomStringUtils.randomAlphabetic(20));
        patient.setDateOfBirth(LocalDate.now());
        patient.setClient(clientService.create(makeClient(RandomStringUtils.randomAlphabetic(15))));
        Patient savedPatient = patientService.create(patient);
        assertThat("Patient was created", savedPatient.getId(), is(notNullValue()));

        savedPatient.setFirstName("update patient name");
        Patient updatedPatient = patientService.update(savedPatient);
        assertThat("Patient was updated", updatedPatient.getId(), is(notNullValue()));
        assertThat("Updated name matches:", updatedPatient.getFirstName(), is(savedPatient.getFirstName()));
    }

    /**
     * Not all required fields
     */
    @Test(expected = ConstraintViolationException.class)
    public void createNotAllRequired() {
        Patient patient = new Patient();
        patientService.create(patient);
    }

    /**
     * bad create
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void badCreate() {
        patientService.create(null);
    }

    /**
     * bad update
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void badUpdate() {
        Patient toUpdate = new Patient();
        toUpdate.setId(1l);
        patientService.update(toUpdate);
    }


    /**
     * Create successfully then find.
     */
    @Test
    public void create() {

        Patient patient = new Patient();
        patient.setFirstName("to find");
        patient.setLastName(RandomStringUtils.randomAlphabetic(20));
        patient.setDateOfBirth(LocalDate.now());
        patient.setClient(clientService.create(makeClient(RandomStringUtils.randomAlphabetic(15))));
        Patient savedPatient = patientService.create(patient);
        assertThat("Patient was created", savedPatient.getId(), is(notNullValue()));

        Page<Patient> page = patientService.list(null, new PatientSearchFilter("toFind"));

                Assert.assertThat("can find the client", patientService.list(null, new PatientSearchFilter("toFind")), hasItem(savedPatient));

    }
}
