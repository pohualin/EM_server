package com.emmisolutions.emmimanager.service.spring.jobs;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.service.jobs.ScheduleProgramReminderEmailJobMaintenanceService;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static com.emmisolutions.emmimanager.service.jobs.AllJobs.PATIENT_EMAIL_JOB_GROUP;
import static com.emmisolutions.emmimanager.service.jobs.AllJobs.SCHEDULED_PROGRAM_REMINDER_EMAIL_JOB_BEAN_NAME;
import static com.emmisolutions.emmimanager.service.jobs.ScheduleProgramReminderEmailJobMaintenanceService.ReminderDay.*;
import static org.joda.time.DateTimeZone.UTC;
import static org.quartz.DateBuilder.IntervalUnit.MINUTE;
import static org.quartz.DateBuilder.futureDate;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.TriggerKey.triggerKey;

/**
 * Days before schedule implementation
 */
@Service
public class ScheduledProgramReminderEmailJobMaintenanceServiceImpl implements ScheduleProgramReminderEmailJobMaintenanceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledProgramReminderEmailJobMaintenanceServiceImpl.class);

    private Scheduler scheduler;

    @Override
    @Transactional
    public void updateScheduledReminders(ScheduledProgram scheduledProgram, String linkUrl, String trackingUrl) {
        // re-schedule for all days without the AT_SCHEDULED day
        schedule(scheduledProgram, linkUrl, trackingUrl, TWO_DAYS_BEFORE_VIEW_BY_DATE,
                FOUR_DAYS_BEFORE_VIEW_BY_DATE, SIX_DAYS_BEFORE_VIEW_BY_DATE,
                EIGHT_DAYS_BEFORE_VIEW_BY_DATE);
    }

    @Override
    @Transactional
    public void scheduleReminders(ScheduledProgram scheduledProgram, String linkUrl, String trackingUrl) {
        // schedule for all reminder days
        schedule(scheduledProgram, linkUrl, trackingUrl, ReminderDay.values());
    }

    @Override
    @Resource(name = "quartzScheduler")
    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public ReminderDay extractReminderDay(JobExecutionContext jobExecutionContext) {
        ReminderDay ret = null;
        if (jobExecutionContext != null && jobExecutionContext.getTrigger() != null) {
            TriggerKey triggerKey = jobExecutionContext.getTrigger().getKey();
            if (triggerKey != null) {
                ret = ReminderDay.fromString(
                        StringUtils.substringBefore(triggerKey.getName(), PATIENT_EMAIL_TRIGGER_NAME_SUFFIX));
            }
        }
        return ret;
    }

    @Override
    public ScheduledProgram extractScheduledProgram(JobExecutionContext jobExecutionContext) {
        ScheduledProgram ret = null;
        if (jobExecutionContext != null && jobExecutionContext.getTrigger() != null) {
            TriggerKey triggerKey = jobExecutionContext.getTrigger().getKey();
            if (triggerKey != null) {
                String id = StringUtils.substringAfter(triggerKey.getGroup(), PATIENT_EMAIL_TRIGGER_GROUP_PREFIX);
                if (StringUtils.isNumeric(id)) {
                    ret = new ScheduledProgram(new Long(id));
                }
            }
        }
        return ret;
    }

    /**
     * Schedules the patient email job.
     *
     * @param scheduledProgram for which to schedule notifications
     * @param days             on which to notify the patient
     */
    private void schedule(ScheduledProgram scheduledProgram, String linkUrl, String trackingUrl, ReminderDay... days) {
        if (scheduledProgram != null && scheduledProgram.getId() != null) {
            try {
                for (ReminderDay notificationDay : days) {
                    // for each notification day, create or update an existing trigger
                    Trigger newTrigger = createTrigger(notificationDay, scheduledProgram, linkUrl, trackingUrl);
                    Trigger existing = scheduler
                            .getTrigger(createTriggerKey(notificationDay, scheduledProgram));
                    if (existing != null) {
                        if (newTrigger != null) {
                            LOGGER.debug("Updating {} with {}", existing, newTrigger);
                            scheduler.rescheduleJob(existing.getKey(), newTrigger);
                        } else {
                            LOGGER.debug("Un-scheduling {}", existing);
                            scheduler.unscheduleJob(existing.getKey());
                        }
                    } else if (newTrigger != null) {
                        LOGGER.debug("Creating {}", newTrigger);
                        scheduler.scheduleJob(newTrigger);
                    }
                }
            } catch (SchedulerException e) {
                throw new RuntimeException("Unable to schedule reminder email for program", e);
            }
        }
    }

    /**
     * Creates a new trigger.
     *
     * @param day              when the job should run, or zero for today
     * @param scheduledProgram to create the trigger key and trigger start date
     * @return a new trigger when the start date is >= today and the scheduled program is active,
     * null when start date < today or program is inactive
     */
    private Trigger createTrigger(ReminderDay day, ScheduledProgram scheduledProgram, String linkUrl, String trackingUrl) {
        Trigger ret = null;
        if (scheduledProgram != null && scheduledProgram.isActive()) {
            LocalDate triggerDate = scheduledProgram.getViewByDate().minusDays(day.getDay());
            if (LocalDate.now(UTC).equals(triggerDate) || LocalDate.now(UTC).isBefore(triggerDate)) {
                // trigger start date is >= today
                TriggerBuilder<SimpleTrigger> simpleTriggerBuilder = newTrigger()
                        .forJob(SCHEDULED_PROGRAM_REMINDER_EMAIL_JOB_BEAN_NAME, PATIENT_EMAIL_JOB_GROUP)
                        .withIdentity(createTriggerKey(day, scheduledProgram))
                        .usingJobData(LINK_URL_KEY, linkUrl)
                        .usingJobData(TRACKING_URL_KEY, trackingUrl)
                        .withSchedule(
                                simpleSchedule()
                                        .withMisfireHandlingInstructionFireNow());
                if (AT_SCHEDULING.equals(day)) {
                    // start the job five minutes from now, just to allow a buffer
                    simpleTriggerBuilder.startAt(futureDate(1, MINUTE));
                } else {
                    // convert to jdk timezone for quartz to run
                    simpleTriggerBuilder.startAt(
                            triggerDate.toDateTimeAtStartOfDay().toDateTime(DateTimeZone.getDefault()).toDate());
                }
                ret = simpleTriggerBuilder.build();
            }
        }
        return ret;
    }

    /**
     * Creates a trigger key.
     *
     * @param day              when the job should run, used for the trigger name
     * @param scheduledProgram on which the job should run, used for the trigger group
     * @return a new trigger key
     */
    TriggerKey createTriggerKey(ReminderDay day, ScheduledProgram scheduledProgram) {
        return triggerKey(
                String.format(PATIENT_EMAIL_TRIGGER_NAME, day.getDay()),
                String.format(PATIENT_EMAIL_TRIGGER_GROUP,
                        scheduledProgram.getId()));
    }
}
