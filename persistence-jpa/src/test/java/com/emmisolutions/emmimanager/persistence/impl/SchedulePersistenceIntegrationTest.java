package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ProgramPersistence;
import com.emmisolutions.emmimanager.persistence.SchedulePersistence;
import org.hamcrest.CoreMatchers;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.data.domain.Page;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Tests the Scheduling persistence
 */
public class SchedulePersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    SchedulePersistence schedulePersistence;

    @Resource
    ProgramPersistence programPersistence;

    /**
     * Happy path save success.
     */
    @Test
    public void save() {
        ScheduledProgram scheduledProgram = new ScheduledProgram();
        Client client = makeNewRandomClient();
        Patient patient = makeNewRandomPatient(client);

        scheduledProgram.setAccessCode("23759604346");
        scheduledProgram.setViewByDate(LocalDate.now(DateTimeZone.UTC));
        scheduledProgram.setLocation(makeNewRandomLocation());
        scheduledProgram.setProvider(makeNewRandomProvider());
        scheduledProgram.setProgram(programPersistence.find(null, null).iterator().next());
        scheduledProgram.setTeam(makeNewRandomTeam(client));
        scheduledProgram.setPatient(patient);

        ScheduledProgram saved = schedulePersistence.save(scheduledProgram);
        assertThat("save happens successfully",
                saved.getId(),
                is(notNullValue()));

        assertThat("access code should not be unique",
                schedulePersistence.isAccessCodeUnique(scheduledProgram.getAccessCode()),
                is(false));

        assertThat("reload works", schedulePersistence.reload(new ScheduledProgram(saved.getId(), null)), is(saved));

        assertThat("reload with team works",
                schedulePersistence.reload(new ScheduledProgram(saved.getId(), new Team(saved.getTeam().getId()))),
                is(saved));

        ScheduledProgram differentTeam = new ScheduledProgram(saved.getId(), makeNewRandomTeam(client));
        assertThat("reload with different team should return null", schedulePersistence.reload(differentTeam),
                is(nullValue()));

        assertThat("reload with patient works", schedulePersistence.findAllByPatient(patient, null).getContent().iterator().next(), is(saved));
    }

    @Test
    public void reloadScenarios(){
        assertThat("reload empty is null", schedulePersistence.reload(new ScheduledProgram()), is(nullValue()));
        assertThat("reload null is null", schedulePersistence.reload(null), is(nullValue()));
    }

    /**
     * Ensures that the access code is validated properly
     */
    @Test(expected = ConstraintViolationException.class)
    public void badAccessCode() {
        ScheduledProgram scheduledProgram = new ScheduledProgram();
        Client client = makeNewRandomClient();

        scheduledProgram.setAccessCode("5A");
        scheduledProgram.setViewByDate(LocalDate.now(DateTimeZone.UTC));
        scheduledProgram.setLocation(makeNewRandomLocation());
        scheduledProgram.setProvider(makeNewRandomProvider());
        scheduledProgram.setProgram(programPersistence.find(null, null).iterator().next());
        scheduledProgram.setTeam(makeNewRandomTeam(client));
        scheduledProgram.setPatient(makeNewRandomPatient(client));

        schedulePersistence.save(scheduledProgram);
    }
}
