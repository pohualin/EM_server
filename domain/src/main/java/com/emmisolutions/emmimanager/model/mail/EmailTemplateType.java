package com.emmisolutions.emmimanager.model.mail;

/**
 * Represents the email template type
 */
public enum EmailTemplateType {
    ACTIVATION, VALIDATION, PASSWORD_RESET_INVALID_ACCOUNT,
    PASSWORD_CHANGED, PASSWORD_RESET, PASSWORD_RESET_NOT_ENABLED,
    SCHEDULED_PROGRAM_PATIENT_REMINDER
}
