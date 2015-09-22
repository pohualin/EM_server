package com.emmisolutions.emmimanager.service.jobs;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import org.quartz.Scheduler;

/**
 * Schedule Program reminder service
 */
public interface ScheduleProgramReminderEmailJobMaintenanceService {

    /**
     * Value used to store the program id in the trigger
     */
    String SCHEDULED_PROGRAM_ID = "scheduledProgramId";

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
}
