package com.emmisolutions.emmimanager.service.mail;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.persistence.EmailTemplatePersistence;
import com.emmisolutions.emmimanager.service.jobs.ScheduleProgramReminderEmailJobMaintenanceService;

/**
 * Manages emails to patients
 */
public interface PatientMailService {

    /**
     * Sends the appropriate reminder email to a patient
     *
     * @param scheduledProgram on which to remind
     * @param day              the reminder day
     */
    void sendReminderEmail(ScheduledProgram scheduledProgram,
                           ScheduleProgramReminderEmailJobMaintenanceService.ReminderDay day);

    /**
     * Sets the email template persistence dependency
     *
     * @param emailTemplatePersistence to use
     */
    void setEmailTemplatePersistence(EmailTemplatePersistence emailTemplatePersistence);
}
