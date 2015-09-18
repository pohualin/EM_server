package com.emmisolutions.emmimanager.service.jobs;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;

/**
 * Schedule Program reminder service
 */
public interface ScheduleProgramReminderEmailJob {

    /**
     * Value used to store the program id
     */
    String SCHEDULED_PROGRAM_ID = "scheduledProgramId";

    /**
     * Triggers the reminder job for every day until the view by date
     *
     * @param scheduledProgram on which to schedule
     */
    void scheduleReminder(ScheduledProgram scheduledProgram);
}
