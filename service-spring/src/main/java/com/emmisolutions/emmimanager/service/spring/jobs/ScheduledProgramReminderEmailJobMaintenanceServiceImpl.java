package com.emmisolutions.emmimanager.service.spring.jobs;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.service.jobs.ScheduleProgramReminderEmailJobMaintenanceService;
import org.joda.time.LocalDate;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static com.emmisolutions.emmimanager.service.jobs.AllJobs.PATIENT_EMAIL_JOB_GROUP;
import static com.emmisolutions.emmimanager.service.jobs.AllJobs.SCHEDULED_PROGRAM_REMINDER_EMAIL;
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
    public void updateScheduledReminders(ScheduledProgram scheduledProgram) {
        schedule(scheduledProgram, 2, 4, 6, 8);
    }

    @Override
    @Transactional
    public void scheduleReminders(ScheduledProgram scheduledProgram) {
        schedule(scheduledProgram, 0, 2, 4, 6, 8);
    }

    @Resource(name = "quartzScheduler")
    public void setScheduler(Scheduler scheduler){
        this.scheduler = scheduler;
    }

    /**
     * Schedules the patient email job.
     *
     * @param scheduledProgram for which to schedule notifications
     * @param days             on which to notify the patient
     */
    private void schedule(ScheduledProgram scheduledProgram, int... days) {
        if (scheduledProgram != null && scheduledProgram.getId() != null) {
            try {
                for (int notificationDay : days) {
                    // for each notification day, create or update an existing trigger
                    Trigger newTrigger = createTrigger(notificationDay, scheduledProgram);
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
    private Trigger createTrigger(int day, ScheduledProgram scheduledProgram) {
        Trigger ret = null;
        if (scheduledProgram != null && scheduledProgram.isActive()) {
            LocalDate triggerDate = scheduledProgram.getViewByDate().minusDays(day);
            if (LocalDate.now(UTC).equals(triggerDate) || LocalDate.now(UTC).isBefore(triggerDate)) {
                // trigger start date is >= today
                TriggerBuilder<SimpleTrigger> simpleTriggerBuilder = newTrigger()
                        .forJob(SCHEDULED_PROGRAM_REMINDER_EMAIL, PATIENT_EMAIL_JOB_GROUP)
                        .withIdentity(createTriggerKey(day, scheduledProgram))
                        .withSchedule(
                                simpleSchedule()
                                        .withMisfireHandlingInstructionFireNow())
                        .usingJobData(SCHEDULED_PROGRAM_ID, scheduledProgram.getId().toString());
                if (day == 0) {
                    simpleTriggerBuilder.startAt(futureDate(10, MINUTE));
                } else {
                    simpleTriggerBuilder.startAt(triggerDate.toDate());
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
    private TriggerKey createTriggerKey(int day, ScheduledProgram scheduledProgram) {
        return triggerKey(
                String.format(PATIENT_EMAIL_TRIGGER_NAME, day),
                String.format(PATIENT_EMAIL_TRIGGER_GROUP,
                        scheduledProgram.getId()));
    }
}
