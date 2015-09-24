package com.emmisolutions.emmimanager.service.spring.jobs;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.service.BaseUnitTest;
import com.emmisolutions.emmimanager.service.ScheduleService;
import com.emmisolutions.emmimanager.service.mail.PatientMailService;
import com.hazelcast.core.HazelcastInstance;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.Trigger;

import static com.emmisolutions.emmimanager.service.jobs.ScheduleProgramReminderEmailJobMaintenanceService.ReminderDay.AT_SCHEDULING;

/**
 * Make sure the job scheduler is hooked up properly
 */
public class ScheduledProgramReminderJobSchedulerUnitTest extends BaseUnitTest {


    /**
     * Make sure that execute() on the quartz bean calls the patient mail service
     *
     * @param patientMailService  a mock
     * @param scheduler           a mock
     * @param jobExecutionContext a mock
     * @throws JobExecutionException shouldn't happen.. scheduler api
     */
    @Test
    public void happyPath(@Mocked final PatientMailService patientMailService,
                          @Mocked final Scheduler scheduler,
                          @Mocked final JobExecutionContext jobExecutionContext,
                          @Mocked final Trigger trigger,
                          @Mocked final HazelcastInstance hazelcastInstance,
                          @Mocked final ScheduleService scheduleService,
                          @Mocked final ScheduledProgram scheduledProgram) throws JobExecutionException {

        // setup the dependencies and data
        final ScheduledProgramReminderEmailJobMaintenanceServiceImpl scheduledProgramReminderEmailJobMaintenanceService =
                new ScheduledProgramReminderEmailJobMaintenanceServiceImpl();
        final Patient patient = new Patient(1l);
        new NonStrictExpectations() {{
            scheduleService.reload(scheduledProgram);
            returns(scheduledProgram);
            scheduledProgram.getPatient();
            returns(patient);
            jobExecutionContext.getTrigger();
            returns(trigger);
            // make sure the trigger key in the mock jobExecutionContext uses the scheduled program for day zero
            trigger.getKey();
            returns(scheduledProgramReminderEmailJobMaintenanceService
                    .createTriggerKey(AT_SCHEDULING, scheduledProgram));
        }};

        // execute the job the way the scheduler would
        ScheduledProgramReminderJobScheduler svc = new ScheduledProgramReminderJobScheduler();
        svc.patientMailService = patientMailService;
        svc.scheduler = scheduler;
        svc.scheduleService = scheduleService;
        svc.hazelCast = hazelcastInstance;
        svc.scheduleProgramReminderEmailJobMaintenanceService = scheduledProgramReminderEmailJobMaintenanceService;
        svc.execute(jobExecutionContext);

        // verify that the reminder email was sent
        new Verifications() {{
            patientMailService.sendReminderEmail(scheduledProgram, AT_SCHEDULING);
            times = 1;
        }};
    }
}
