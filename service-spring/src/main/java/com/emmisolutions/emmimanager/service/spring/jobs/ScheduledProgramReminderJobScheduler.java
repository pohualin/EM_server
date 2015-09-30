package com.emmisolutions.emmimanager.service.spring.jobs;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.service.ScheduleService;
import com.emmisolutions.emmimanager.service.jobs.ScheduleProgramReminderEmailJobMaintenanceService;
import com.emmisolutions.emmimanager.service.mail.PatientMailService;
import com.hazelcast.core.HazelcastInstance;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.locks.Lock;

import static com.emmisolutions.emmimanager.service.jobs.AllJobs.PATIENT_EMAIL_JOB_GROUP;
import static com.emmisolutions.emmimanager.service.jobs.AllJobs.SCHEDULED_PROGRAM_REMINDER_EMAIL_JOB_BEAN_NAME;
import static com.emmisolutions.emmimanager.service.jobs.ScheduleProgramReminderEmailJobMaintenanceService.*;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.JobKey.jobKey;

/**
 * Job creation and execution for scheduled email programs
 */
@Component(SCHEDULED_PROGRAM_REMINDER_EMAIL_JOB_BEAN_NAME)
public class ScheduledProgramReminderJobScheduler extends QuartzJobBean {

    @Resource
    PatientMailService patientMailService;

    @Resource(name = "quartzScheduler")
    Scheduler scheduler;

    @Resource
    ScheduleProgramReminderEmailJobMaintenanceService scheduleProgramReminderEmailJobMaintenanceService;

    @Resource(name = "hazelCast")
    HazelcastInstance hazelCast;

    @Resource
    ScheduleService scheduleService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        ScheduledProgram scheduledProgram = scheduleService.reload(
                scheduleProgramReminderEmailJobMaintenanceService.extractScheduledProgram(context));

        if (scheduledProgram != null) {
            // acquire a cluster aware lock based upon the patient id
            Lock clusterAwareLock = hazelCast.getLock(
                    String.format(PATIENT_EMAIL_CLUSTER_LOCK_NAME, scheduledProgram.getPatient().getId()));
            /*
                Lock the patient for this email type. Note: this will only work if the sendReminderEmail
                call is not @Async.. because the writing to the email tracking table happens inside of
                sendReminderEmail.. if the call comes back before the tracking record is created,
                another email will go out, which is undesirable.
              */
            clusterAwareLock.lock();
            try {
                patientMailService.sendReminderEmail(scheduledProgram,
                        scheduleProgramReminderEmailJobMaintenanceService.extractReminderDay(context),
                        context.getMergedJobDataMap().getString(LINK_URL_KEY),
                        context.getMergedJobDataMap().getString(TRACKING_URL_KEY));
            } finally {
                // let the next thread that was waiting on this lock continue
                clusterAwareLock.unlock();
            }
        }
    }

    /**
     * Creates the persistent job to be triggered by the scheduleReminder method.
     */
    @PostConstruct
    private void createJob() throws SchedulerException {
        if (!scheduler.checkExists(jobKey(
                SCHEDULED_PROGRAM_REMINDER_EMAIL_JOB_BEAN_NAME, PATIENT_EMAIL_JOB_GROUP))) {
            scheduler.addJob(
                    newJob(ScheduledProgramReminderJobScheduler.class)
                            .withIdentity(SCHEDULED_PROGRAM_REMINDER_EMAIL_JOB_BEAN_NAME,
                                    PATIENT_EMAIL_JOB_GROUP)
                            .requestRecovery()
                            .storeDurably()
                            .build(), false);
        }
    }
}
