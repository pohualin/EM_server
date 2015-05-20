package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ProgramPersistence;
import com.emmisolutions.emmimanager.persistence.SchedulePersistence;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.junit.Test;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
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

        scheduledProgram.setAccessCode("23759604346");
        scheduledProgram.setViewByDate(LocalDate.now(DateTimeZone.UTC));
        scheduledProgram.setLocation(makeNewRandomLocation());
        scheduledProgram.setProgram(programPersistence.find(null, null).iterator().next());
        scheduledProgram.setTeam(makeNewRandomTeam(client));
        scheduledProgram.setPatient(makeNewRandomPatient(client));

        assertThat("save happens successfully",
                schedulePersistence.save(scheduledProgram).getId(),
                is(notNullValue()));

        assertThat("access code should not be unique",
                schedulePersistence.isAccessCodeUnique(scheduledProgram.getAccessCode()),
                is(false));

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
        scheduledProgram.setProgram(programPersistence.find(null, null).iterator().next());
        scheduledProgram.setTeam(makeNewRandomTeam(client));
        scheduledProgram.setPatient(makeNewRandomPatient(client));

        schedulePersistence.save(scheduledProgram);
    }
}
