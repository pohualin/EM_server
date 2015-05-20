package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ProgramService;
import com.emmisolutions.emmimanager.service.ScheduleService;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Tests the schedule service
 */
public class ScheduleServiceIntegrationTest extends BaseIntegrationTest {

    @Resource
    ScheduleService scheduleService;

    @Resource
    ProgramService programService;

    /**
     * Call the method with a null.
     */
    @Test
    public void saveNull() {
        assertThat("scheduling a null should return null", scheduleService.schedule(null), is(nullValue()));
    }

    /**
     * Happy path save.
     */
    @Test
    public void valid() {
        ScheduledProgram scheduledProgram = new ScheduledProgram();
        Client client = makeNewRandomClient();

        String shouldBeOverwrittenAccessCode = "23759604346";
        scheduledProgram.setAccessCode(shouldBeOverwrittenAccessCode);
        scheduledProgram.setViewByDate(LocalDate.now(DateTimeZone.UTC));
        scheduledProgram.setLocation(makeNewRandomLocation());
        scheduledProgram.setProgram(programService.find(null, null).iterator().next());
        scheduledProgram.setTeam(makeNewRandomTeam(client));
        scheduledProgram.setPatient(makeNewRandomPatient(client));

        assertThat("save happens successfully",
                scheduleService.schedule(scheduledProgram).getAccessCode(),
                is(not(shouldBeOverwrittenAccessCode)));
    }

    /**
     * Tests when the team and patient point to different clients
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void differentClient() {
        ScheduledProgram scheduledProgram = new ScheduledProgram();
        scheduledProgram.setViewByDate(LocalDate.now(DateTimeZone.UTC).plusDays(1));
        scheduledProgram.setLocation(makeNewRandomLocation());
        scheduledProgram.setProgram(programService.find(null, null).iterator().next());
        scheduledProgram.setTeam(makeNewRandomTeam(null));
        scheduledProgram.setPatient(makeNewRandomPatient(null));
        scheduleService.schedule(scheduledProgram);
    }

    /**
     * View by date cannot be in the past
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void viewByDateNotInTheFuture() {
        ScheduledProgram scheduledProgram = new ScheduledProgram();
        Client client = makeNewRandomClient();
        scheduledProgram.setViewByDate(LocalDate.now(DateTimeZone.UTC).minusDays(1));
        scheduledProgram.setLocation(makeNewRandomLocation());
        scheduledProgram.setProgram(programService.find(null, null).iterator().next());
        scheduledProgram.setTeam(makeNewRandomTeam(client));
        scheduledProgram.setPatient(makeNewRandomPatient(client));
        scheduleService.schedule(scheduledProgram);
    }

}
