package com.emmisolutions.emmimanager.service.jobs;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;

/**
 * Schedule Program reminder service
 */
public interface ScheduleProgramReminderEmailJobMaintenanceService {


    /**
     * Group name for patient email triggers
     */
    String PATIENT_EMAIL_TRIGGER_GROUP = "SCHEDULED_PROGRAM_%s";

    /**
     * Name for patient email triggers
     */
    String PATIENT_EMAIL_TRIGGER_NAME = "%s_DAY_REMINDER_EMAIL";

    /**
     * Schedules email reminders for a new scheduled program.
     * This will create zero day as well as the other
     * reminders.
     *
     * @param scheduledProgram on which to schedule
     */
    void scheduleReminders(ScheduledProgram scheduledProgram);

    /**
     * Update the scheduled reminders for the program
     *
     * @param scheduledProgram to update the reminders
     */
    void updateScheduledReminders(ScheduledProgram scheduledProgram);

    /**
     * Sets the scheduler
     *
     * @param scheduler to use
     */
    void setScheduler(Scheduler scheduler);

    /**
     * Pull out the reminder day from a fired job
     *
     * @param jobExecutionContext the fired job context
     * @return the ReminderDay associated with the job or null
     */
    ReminderDay extractDay(JobExecutionContext jobExecutionContext);

    /**
     * Extract a scheduled program (not persistent) from the fired job
     *
     * @param jobExecutionContext the fired job context
     * @return non-persistent ScheduledProgram
     */
    ScheduledProgram extractScheduledProgram(JobExecutionContext jobExecutionContext);


    enum ReminderDay {
        AT_SCHEDULING(0), TWO_DAYS_BEFORE_VIEW_BY_DATE(2),
        FOUR_DAYS_BEFORE_VIEW_BY_DATE(4), SIX_DAYS_BEFORE_VIEW_BY_DATE(6),
        EIGHT_DAYS_BEFORE_VIEW_BY_DATE(8);

        private final int day;

        public int getDay(){
            return day;
        }

        ReminderDay(int day) {
            this.day = day;
        }

        public static ReminderDay fromInteger(int day) {
            for (ReminderDay reminderDay : ReminderDay.values()) {
                if (reminderDay.day == day) {
                    return reminderDay;
                }
            }
            return null;
        }
    }
}
