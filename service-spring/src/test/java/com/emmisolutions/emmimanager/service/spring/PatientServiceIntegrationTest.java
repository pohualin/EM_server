package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.PatientService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import static com.emmisolutions.emmimanager.model.PatientSearchFilter.with;
import static org.hamcrest.CoreMatchers.*;
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
    public void testPatientCreateAndReload() {
        assertThat("Patient was created", makeNewRandomPatient(null).getId(), is(notNullValue()));
    }

    @Test
    public void reloadPatient() {
        Patient reloadedPatient = patientService.reload(makeNewRandomPatient(null));
        assertThat("Patient was reloaded", reloadedPatient.getId(), is(notNullValue()));
    }

    @Test
    public void testPatientUpdate() {
        Patient savedPatient = makeNewRandomPatient(null);
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
        Patient savedPatient = makeNewRandomPatient(null);
        assertThat("Patient was created", savedPatient.getId(), is(notNullValue()));
        Assert.assertThat("can find the patient for that client", patientService.list(null,
                        with().client(savedPatient.getClient()).names(savedPatient.getFirstName())),
                hasItem(savedPatient));

    }

    @Test
    public void testList() {
        Patient patient = makeNewRandomPatient(null);
        assertThat("Page of Patients retrieved contains the searched item", patientService.list(null,
                        with().client(patient.getClient()).names(patient.getFirstName())),
                hasItem(patient));
    }
}
