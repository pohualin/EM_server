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
    public void testPatientCreateAndReload() {
        assertThat("Patient was created", makeNewRandomPatient().getId(), is(notNullValue()));
    }

    @Test
    public void reloadPatient() {
        Patient reloadedPatient = patientService.reload(makeNewRandomPatient());
        assertThat("Patient was reloaded", reloadedPatient.getId(), is(notNullValue()));
    }

    @Test
    public void testPatientUpdate() {
        Patient savedPatient = makeNewRandomPatient();
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
        Patient savedPatient = makeNewRandomPatient();
        assertThat("Patient was created", savedPatient.getId(), is(notNullValue()));
        Assert.assertThat("can find the patient for that client", patientService.list(null, new PatientSearchFilter(savedPatient.getClient(), savedPatient.getFirstName())), hasItem(savedPatient));

    }

    @Test
    public void testList() {
        Patient patient = makeNewRandomPatient();
        PatientSearchFilter filter = new PatientSearchFilter(patient.getClient(), patient.getFirstName());
        Page<Patient> patientPage = patientService.list(null, filter);
        assertThat("Page of Patients retrieved contains the searched item", patientPage, hasItem(patient));
    }
}
