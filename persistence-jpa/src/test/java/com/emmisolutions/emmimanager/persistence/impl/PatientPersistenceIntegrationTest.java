package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.PatientPersistence;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import javax.annotation.Resource;

import static com.emmisolutions.emmimanager.model.PatientSearchFilter.with;
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
    public void createAndReload() {
        Patient reloadedPatient = patientPersistence.reload(makeNewRandomPatient(null));
        assertThat("Patient was reloaded", reloadedPatient.getId(), is(notNullValue()));
    }

    @Test
    public void basicList() {
        Patient patient = makeNewRandomPatient(null);
        assertThat("Page of Patients retrieved contains the searched item",
                patientPersistence.list(null,
                        with().client(patient.getClient()).names(patient.getFirstName())),
                hasItem(patient));
    }

    @Test
    public void filterByPhone() {
        Patient patient = makeNewRandomPatient(null);
        assertThat("patient should be found by exact phone number",
                patientPersistence.list(null, with().phone(patient.getPhone())),
                hasItem(patient));

        assertThat("patient should be found by phone number with random letter as separator",
                patientPersistence.list(null,
                        with().phone(StringUtils.replace(patient.getPhone(), "-",
                                RandomStringUtils.randomAlphabetic(1)))),
                hasItem(patient));

        assertThat("Should not find this phone number", patientPersistence.list(null,
                        with().phone("200-200-0000")),
                not(hasItem(patient)));
    }

    @Test
    public void filterByEmail() {
        Patient patient = makeNewRandomPatient(null);
        assertThat("patient should be found by exact email address",
                patientPersistence.list(null, with().email(patient.getEmail())),
                hasItem(patient));

        assertThat("patient should be not found this email address",
                patientPersistence.list(null, with().email("nope@nope.org")),
                not(hasItem(patient)));
    }

    /**
     * Make sure the access code filter works properly
     */
    @Test
    public void filterByAccessCode(){
        final ScheduledProgram scheduledProgram = makeNewRandomScheduledProgram(null, null);
        assertThat("patient should be found by access code",
                patientPersistence.list(null, with().accessCodes(scheduledProgram.getAccessCode())),
                hasItem(scheduledProgram.getPatient()));

        assertThat("patient should be not found this email address",
                patientPersistence.list(null, with().accessCodes("12345678909")),
                not(hasItem(scheduledProgram.getPatient())));

        final ScheduledProgram secondProgram = makeNewRandomScheduledProgram(
                scheduledProgram.getTeam().getClient(), scheduledProgram.getPatient());

        assertThat("same patient should be found by second access code",
                patientPersistence.list(null, with().accessCodes(secondProgram.getAccessCode())),
                hasItem(scheduledProgram.getPatient()));

        assertThat("only one patient should be found when searching with multiple matching access codes",
                patientPersistence.list(null, with().accessCodes(scheduledProgram.getAccessCode(),
                        secondProgram.getAccessCode())).getTotalElements(),
                is(1l));
    }


}
