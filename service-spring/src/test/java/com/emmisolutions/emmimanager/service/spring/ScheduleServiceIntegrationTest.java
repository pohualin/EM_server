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
     * Happy path save and reload/find.
     */
    @Test
    public void valid() {
        ScheduledProgram scheduledProgram = new ScheduledProgram();
        Client client = makeNewRandomClient();

        String accessCode = "23759604346";
        scheduledProgram.setAccessCode(accessCode);
        scheduledProgram.setViewByDate(LocalDate.now(DateTimeZone.UTC));
        scheduledProgram.setLocation(makeNewRandomLocation());
        scheduledProgram.setProvider(makeNewRandomProvider());
        scheduledProgram.setProgram(programService.find(null, null).iterator().next());
        scheduledProgram.setTeam(makeNewRandomTeam(client));
        scheduledProgram.setPatient(makeNewRandomPatient(client));

        ScheduledProgram saved = scheduleService.schedule(scheduledProgram);
        assertThat("save happens successfully, access code is generated/overwritten",
                saved.getAccessCode(),
                is(not(accessCode)));

        assertThat("reload works", scheduleService.reload(new ScheduledProgram(saved.getId(), null)), is(saved));

        assertThat("find by patient works", scheduleService.findAllByPatient(scheduledProgram.getPatient(), null), hasItem(saved));

        assertThat("find by null patient should not find anything", scheduleService.findAllByPatient(null, null), is(nullValue()));

        saved.setAccessCode("29999999999");
        ScheduledProgram updated = scheduleService.update(saved);

        assertThat("updated and original are the same", updated, is(saved));
        assertThat("version is up by one", updated.getVersion(), is(saved.getVersion() + 1));
    }

    /**
     * Make sure update fails when the scheduled program cannot be found
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void badUpdate() {
        scheduleService.update(new ScheduledProgram());
    }

    /**
     * Update of null should throw error
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void nullUpdate() {
        scheduleService.update(null);
    }

    /**
     * Tests when the team and patient point to different clients
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void differentClient() {
        ScheduledProgram scheduledProgram = new ScheduledProgram();
        scheduledProgram.setViewByDate(LocalDate.now(DateTimeZone.UTC).plusDays(1));
        scheduledProgram.setLocation(makeNewRandomLocation());
        scheduledProgram.setProvider(makeNewRandomProvider());
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
        scheduledProgram.setProvider(makeNewRandomProvider());
        scheduledProgram.setProgram(programService.find(null, null).iterator().next());
        scheduledProgram.setTeam(makeNewRandomTeam(client));
        scheduledProgram.setPatient(makeNewRandomPatient(client));
        scheduleService.schedule(scheduledProgram);
    }

}
