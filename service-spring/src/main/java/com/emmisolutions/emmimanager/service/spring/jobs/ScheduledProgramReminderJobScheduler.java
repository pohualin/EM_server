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
import static com.emmisolutions.emmimanager.service.jobs.AllJobs.SCHEDULED_PROGRAM_REMINDER_EMAIL;
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
            // only allow one scheduled program job per patient to run concurrently
            Lock clusterAwareLock = hazelCast.getLock(
                    String.format("PATIENT_SCHEDULED_PROGRAM_EMAIL_NOTIFICATION_%s",
                            scheduledProgram.getPatient().getId()));
            clusterAwareLock.lock();
            try {
                patientMailService.sendReminderEmail(scheduledProgram,
                        scheduleProgramReminderEmailJobMaintenanceService.extractReminderDay(context));
            } finally {
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
