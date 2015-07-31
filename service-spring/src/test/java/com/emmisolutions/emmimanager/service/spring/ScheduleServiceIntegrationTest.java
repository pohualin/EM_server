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
import static org.junit.Assert.fail;

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
    }

    /**
     * Ensure that only active and view-by-date can be updated
     */
    @Test
    public void update() {
        ScheduledProgram saved = makeNewScheduledProgram(null);

        ScheduledProgram forUpdate = new ScheduledProgram(saved.getId());
        forUpdate.setVersion(saved.getVersion());
        forUpdate.setAccessCode("29999999999");
        forUpdate.setActive(false);
        forUpdate.setViewByDate(saved.getViewByDate());

        ScheduledProgram updated = scheduleService.update(forUpdate);

        assertThat("updated and original are the same", updated, is(saved));
        assertThat("version is up by one", updated.getVersion(), is(saved.getVersion() + 1));
        assertThat("access code is not overwritten on update",
                updated.getAccessCode(),
                is(saved.getAccessCode()));
        assertThat("program is set from db",
                updated.getProgram(),
                is(saved.getProgram()));
    }

    /**
     * Make sure view by date is validated on update
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void updateInvalidDate() {
        ScheduledProgram saved = makeNewScheduledProgram(null);

        // view-by-date should still be validated on update
        saved.setViewByDate(LocalDate.now(DateTimeZone.UTC).minusDays(1));
        scheduleService.update(saved);
        fail("the update call should have failed due to invalid view-by-date");
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
    
    /**
     * Phone and Email is required
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void phoneNotProvided() {
        ScheduledProgram scheduledProgram = new ScheduledProgram();
        Client client = makeNewRandomClient();
        scheduledProgram.setViewByDate(LocalDate.now(DateTimeZone.UTC));
        scheduledProgram.setLocation(makeNewRandomLocation());
        scheduledProgram.setProvider(makeNewRandomProvider());
        scheduledProgram.setProgram(programService.find(null, null).iterator().next());
        scheduledProgram.setTeam(makeNewRandomTeamWithConfiguration(client));
        scheduledProgram.setPatient(makeNewRandomPatient(client));
        scheduleService.schedule(scheduledProgram);
    }

}
