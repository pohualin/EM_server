package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.PatientPersistence;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.data.domain.Page;

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
                        with().client(new Client(patient.getClient().getId())).names(patient.getFirstName())),
                hasItem(patient));
    }

    @Test
    public void filterByPhone() {
        Patient patient = makeNewRandomPatient(null);
        assertThat("patient should be found by exact phone number",
                patientPersistence.list(null, with().phones(patient.getPhone())),
                hasItem(patient));

        assertThat("patient should be found by phone number with random letter as separator",
                patientPersistence.list(null,
                        with().phones(StringUtils.replace(patient.getPhone(), "-",
                                RandomStringUtils.randomAlphabetic(1)))),
                hasItem(patient));

        assertThat("Should not find this phone number", patientPersistence.list(null,
                        with().phones("200-200-0000")),
                not(hasItem(patient)));

        Patient patient2 = makeNewRandomPatient(null);

        assertThat("both patients should be found when searching across phones",
                patientPersistence.list(null, with().phones(patient.getPhone(), patient2.getPhone())),
                hasItems(patient, patient2));

        assertThat("both patients should not be repeated",
                patientPersistence.list(null, with().phones(patient.getPhone(), patient2.getPhone())).getTotalElements(),
                is(2l));
    }

    @Test
    public void filterByEmail() {
        Patient patient = makeNewRandomPatient(null);
        assertThat("patient should be found by exact email address",
                patientPersistence.list(null, with().emails(patient.getEmail())),
                hasItem(patient));

        assertThat("patient should be not found this email address",
                patientPersistence.list(null, with().emails("nope@nope.org")),
                not(hasItem(patient)));

        Patient patient2 = makeNewRandomPatient(null);

        assertThat("both patients should be found when searching across emails",
                patientPersistence.list(null, with().emails(patient.getEmail(), patient2.getEmail())),
                hasItems(patient, patient2));

        assertThat("both patients should not be repeated",
                patientPersistence.list(null, with().emails(patient.getEmail(), patient2.getEmail())).getTotalElements(),
                is(2l));

    }

    /**
     * Make sure the access code filter works properly
     */
    @Test
    public void filterByAccessCode() {
        final ScheduledProgram scheduledProgram = makeNewRandomScheduledProgram(null, null, null);
        assertThat("patient should be found by access code",
                patientPersistence.list(null, with().accessCodes(scheduledProgram.getAccessCode())),
                hasItem(scheduledProgram.getPatient()));

        assertThat("patient should be not found this email address",
                patientPersistence.list(null, with().accessCodes("12345678909")),
                not(hasItem(scheduledProgram.getPatient())));

        final ScheduledProgram secondProgram = makeNewRandomScheduledProgram(
                scheduledProgram.getTeam().getClient(), scheduledProgram.getPatient(), null);

        assertThat("same patient should be found by second access code",
                patientPersistence.list(null, with().accessCodes(secondProgram.getAccessCode())),
                hasItem(scheduledProgram.getPatient()));

        assertThat("only one patient should be found when searching with multiple matching access codes",
                patientPersistence.list(null, with().accessCodes(scheduledProgram.getAccessCode(),
                        secondProgram.getAccessCode())).getTotalElements(),
                is(1l));
    }

    /**
     * This tests the team filter searching
     */
    @Test
    public void teamFilters() {
        // schedule for one patient on one program on one team and two programs on a another team
        final ScheduledProgram team1Program1 = makeNewRandomScheduledProgram(null, null, null);
        final ScheduledProgram team2Program1 = makeNewRandomScheduledProgram(
                team1Program1.getTeam().getClient(), team1Program1.getPatient(), null);
        final ScheduledProgram team2Program2 = makeNewRandomScheduledProgram(
                team2Program1.getTeam().getClient(), team2Program1.getPatient(), team2Program1.getTeam());

        // schedule one program for new patient on the same team as the other patient
        final ScheduledProgram patient2Team2 = makeNewRandomScheduledProgram(
                team2Program1.getTeam().getClient(), null, team2Program2.getTeam());

        // find them all with one query
        assertThat("both patients should come back",
                patientPersistence.list(null, with()
                        .teams(team1Program1.getTeam(), patient2Team2.getTeam(),
                                team2Program1.getTeam(), team2Program2.getTeam())),
                hasItems(patient2Team2.getPatient(), team1Program1.getPatient()));

        assertThat("only two total patients should come back",
                patientPersistence.list(null, with()
                                .teams(team1Program1.getTeam(), patient2Team2.getTeam(),
                                        team2Program1.getTeam(), team2Program2.getTeam())
                ).getTotalElements(),
                is(2l));
    }

    @Test
    public void loadLatestScheduledProgram() {
        ScheduledProgram lastScheduledProgram = makeNewRandomScheduledProgram(null, null, null);
        Patient patient = lastScheduledProgram.getPatient();
        for (int i = 0; i < 5; i++) {
            lastScheduledProgram = makeNewRandomScheduledProgram(lastScheduledProgram.getTeam().getClient(),
                    lastScheduledProgram.getPatient(), lastScheduledProgram.getTeam());
        }

        Page<Patient> patients = patientPersistence.list(null,
                with().lastScheduledProgramLoaded().teams(lastScheduledProgram.getTeam()));

        assertThat("The patient should be nulled out on the last scheduled program",
                lastScheduledProgram.getPatient(), is(nullValue()));

        assertThat("patient with a bunch of scheduled programs is found", patients,
                hasItem(patient));

        assertThat("last scheduled program is set", patients.iterator().next().getScheduledPrograms(),
                hasItem(lastScheduledProgram));
    }

}
