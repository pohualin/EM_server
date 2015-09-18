package com.emmisolutions.emmimanager.service.spring.jobs;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.service.jobs.AllJobs;
import com.emmisolutions.emmimanager.service.jobs.ScheduleProgramReminderEmailJob;
import com.emmisolutions.emmimanager.service.mail.PatientMailService;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import static com.emmisolutions.emmimanager.service.jobs.AllJobs.SCHEDULED_PROGRAM_REMINDER_EMAIL;
import static org.quartz.CalendarIntervalScheduleBuilder.calendarIntervalSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.JobKey.jobKey;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.TriggerKey.triggerKey;

/**
 * Schedules and executes a job to clean the hli search cache.
 */
@Profile("!test")
@Service(SCHEDULED_PROGRAM_REMINDER_EMAIL)
public class ScheduledProgramReminderEmailJobImpl extends QuartzJobBean implements AllJobs, ScheduleProgramReminderEmailJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledProgramReminderEmailJobImpl.class);

    @Resource(name = "quartzScheduler")
    Scheduler scheduler;
    @Resource
    PatientMailService patientMailService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        patientMailService.sendReminderEmail(new ScheduledProgram(context.getMergedJobDataMap().getLongValue(SCHEDULED_PROGRAM_ID)));
    }

    @Override
    @Transactional
    public void scheduleReminder(ScheduledProgram scheduledProgram) {
        if (scheduledProgram != null && scheduledProgram.getId() != null) {
            try {
                TriggerKey triggerKey = triggerKey(SCHEDULED_PROGRAM_REMINDER_EMAIL + "_" + scheduledProgram.getId(),
                        PATIENT_EMAIL_GROUP);
                Trigger existing = scheduler.getTrigger(triggerKey);
                if (existing == null && scheduledProgram.isActive() &&
                        LocalDate.now(DateTimeZone.UTC).isBefore(scheduledProgram.getViewByDate())) {
                    LOGGER.debug("Creating new email reminder trigger for {}", scheduledProgram);
                    scheduler.scheduleJob(newTrigger()
                            .forJob(SCHEDULED_PROGRAM_REMINDER_EMAIL, PATIENT_EMAIL_GROUP)
                            .usingJobData(SCHEDULED_PROGRAM_ID, scheduledProgram.getId().toString())
                            .withIdentity(triggerKey)
                            .withSchedule(calendarIntervalSchedule().withIntervalInDays(1)
                                    .withMisfireHandlingInstructionFireAndProceed())
                            .startNow()
                            .endAt(scheduledProgram.getViewByDate().toDate())
                            .build());
                } else {
                    if (existing != null && scheduledProgram.isActive() &&
                            LocalDate.now(DateTimeZone.UTC).isBefore(scheduledProgram.getViewByDate())) {
                        LOGGER.debug("Updating existing reminder trigger for {}", scheduledProgram);
                        scheduler.rescheduleJob(existing.getKey(),
                                existing.getTriggerBuilder()
                                        .endAt(scheduledProgram.getViewByDate().toDate()).build());
                    } else {
                        LOGGER.debug("Removing existing reminder trigger for {}", scheduledProgram);
                        scheduler.unscheduleJob(triggerKey);
                    }
                }
            } catch (SchedulerException e) {
                throw new RuntimeException("Unable to schedule reminder email for program", e);
            }
        }
    }

    /**
     * Creates the persistent job to be triggered by the scheduleReminder method.
     */
    @PostConstruct
    private void createJob() throws SchedulerException {
        if (!scheduler.checkExists(jobKey(
                SCHEDULED_PROGRAM_REMINDER_EMAIL, PATIENT_EMAIL_GROUP))) {
            scheduler.addJob(
                    newJob(ScheduledProgramReminderEmailJobImpl.class)
                            .withIdentity(SCHEDULED_PROGRAM_REMINDER_EMAIL,
                                    PATIENT_EMAIL_GROUP)
                            .requestRecovery()
                            .storeDurably()
                            .build(), false);
        }
    }
}
