package com.emmisolutions.emmimanager.service.spring.jobs;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.jobs.ScheduleProgramReminderEmailJobMaintenanceService;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;
import org.junit.Test;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

import javax.annotation.Resource;
import java.util.ArrayList;

import static mockit.Deencapsulation.setField;

/**
 * Test the program reminder scheduler helper implementation service
 */
public class ScheduledProgramReminderEmailJobMaintenanceServiceImplIntegrationTest extends BaseIntegrationTest {

    @Resource
    ScheduleProgramReminderEmailJobMaintenanceService service;

    /**
     * Make sure that emails are scheduled for a newly scheduled program
     *
     * @param scheduler a mocked object
     * @throws SchedulerException shouldn't happen, there because of expectations and verifications
     */
    @Test
    public void newScheduledProgram(@Mocked final Scheduler scheduler) throws SchedulerException {
        new NonStrictExpectations() {{
            // ensure scheduler will return null when existing triggers are fetched
            scheduler.getTrigger(withInstanceOf(TriggerKey.class));
            returns(null);
        }};

        service.setScheduler(scheduler);
        makeNewScheduledProgram(null); // creating a scheduled program should cause job scheduling

        new Verifications() {{
            // verify that the makeNewScheduledProgram actually called scheduleJob
            scheduler.scheduleJob(withInstanceOf(Trigger.class));
            times = 5; // ensures 0, 2, 4, 6, 8 day are created
        }};
    }

    /**
     * Make sure that emails are scheduled for an updated program
     *
     * @param scheduler a mocked object
     * @throws SchedulerException shouldn't happen, there because of verifications
     */
    @Test
    public void updateExistingScheduledProgram(@Mocked final Scheduler scheduler) throws SchedulerException {

        // by default all mocked methods will return an object e.g. scheduler.getTrigger()

        service.setScheduler(scheduler);
        makeNewScheduledProgram(null); // creating a scheduled program should cause job scheduling

        new Verifications() {{
            // verify that the makeNewScheduledProgram actually called rescheduleJob()
            scheduler.rescheduleJob(withInstanceOf(TriggerKey.class), withInstanceOf(Trigger.class));
            times = 5; // ensures 0, 2, 4, 6, 8 day are created
        }};
    }

    /**
     * Ensure un-scheduling occurs when a scheduled program is not active
     *
     * @param scheduler a mocked object
     * @throws SchedulerException shouldn't happen, there because of verifications
     */
    @Test
    public void deactivatedScheduledProgram(@Mocked final Scheduler scheduler) throws SchedulerException {
        // create a scheduled program
        ScheduledProgram scheduledProgram = makeNewScheduledProgram(null);
        scheduledProgram.setActive(false);

        // now push in the mock instead of the normal scheduler
        service.setScheduler(scheduler);

        // do the update to the scheduled reminders
        service.updateScheduledReminders(scheduledProgram);

        new Verifications() {{
            // verify that the makeNewScheduledProgram actually called scheduleJob
            scheduler.unscheduleJob(withInstanceOf(TriggerKey.class));
            times = 4; // ensures 2, 4, 6, 8 day are removed but 0 should stay
        }};
    }

    /**
     * When an exception occurs in the scheduler methods, they are wrapped with a runtime
     * exception instead of the checked SchedulerException
     *
     * @param scheduler a mocked object
     * @throws SchedulerException shouldn't happen, there because of expectations
     */
    @Test(expected = RuntimeException.class)
    public void makeSureRuntimeExceptionIsThrownOnSchedulerError(@Mocked final Scheduler scheduler) throws SchedulerException {
        new NonStrictExpectations() {{
            scheduler.rescheduleJob(withInstanceOf(TriggerKey.class), withInstanceOf(Trigger.class));
            result = new RuntimeException();
        }};
        service.setScheduler(scheduler);
        makeNewScheduledProgram(null);
    }

}
