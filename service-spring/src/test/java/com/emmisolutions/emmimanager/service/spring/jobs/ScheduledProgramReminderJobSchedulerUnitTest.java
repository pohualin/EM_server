package com.emmisolutions.emmimanager.service.spring.jobs;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.service.BaseUnitTest;
import com.emmisolutions.emmimanager.service.mail.PatientMailService;
import mockit.Mocked;
import mockit.Verifications;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;

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
                          @Mocked final JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ScheduledProgramReminderJobScheduler svc = new ScheduledProgramReminderJobScheduler();
        svc.patientMailService = patientMailService;
        svc.scheduler = scheduler;
        svc.execute(jobExecutionContext);

        new Verifications() {{
            patientMailService.sendReminderEmail(withInstanceOf(ScheduledProgram.class));
            times = 1;
        }};
    }
}
