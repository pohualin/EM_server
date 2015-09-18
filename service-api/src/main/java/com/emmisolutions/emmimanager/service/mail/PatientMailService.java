package com.emmisolutions.emmimanager.service.mail;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;

/**
 * Manages emails to patients
 */
public interface PatientMailService {

    /**
     * Sends the appropriate reminder email to a patient
     *
     * @param scheduledProgram on which to remind
     */
    void sendReminderEmail(ScheduledProgram scheduledProgram);
}
