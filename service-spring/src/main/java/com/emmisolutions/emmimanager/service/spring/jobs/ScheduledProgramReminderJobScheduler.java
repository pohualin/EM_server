package com.emmisolutions.emmimanager.service.spring.jobs;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.service.mail.PatientMailService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import static com.emmisolutions.emmimanager.service.jobs.AllJobs.PATIENT_EMAIL_JOB_GROUP;
import static com.emmisolutions.emmimanager.service.jobs.AllJobs.SCHEDULED_PROGRAM_REMINDER_EMAIL;
import static com.emmisolutions.emmimanager.service.jobs.ScheduleProgramReminderEmailJobMaintenanceService.SCHEDULED_PROGRAM_ID;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.JobKey.jobKey;

/**
 * Job creation and execution for scheduled email programs
 */
@Component(SCHEDULED_PROGRAM_REMINDER_EMAIL)
public class ScheduledProgramReminderJobScheduler extends QuartzJobBean {

    @Resource
    PatientMailService patientMailService;

    @Resource(name = "quartzScheduler")
    Scheduler scheduler;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        patientMailService.sendReminderEmail(
                new ScheduledProgram(context.getMergedJobDataMap().getLongValue(SCHEDULED_PROGRAM_ID)));
    }

    /**
     * Creates the persistent job to be triggered by the scheduleReminder method.
     */
    @PostConstruct
    private void createJob() throws SchedulerException {
        if (!scheduler.checkExists(jobKey(
                SCHEDULED_PROGRAM_REMINDER_EMAIL, PATIENT_EMAIL_JOB_GROUP))) {
            scheduler.addJob(
                    newJob(ScheduledProgramReminderJobScheduler.class)
                            .withIdentity(SCHEDULED_PROGRAM_REMINDER_EMAIL,
                                    PATIENT_EMAIL_JOB_GROUP)
                            .requestRecovery()
                            .storeDurably()
                            .build(), false);
        }
    }
}
