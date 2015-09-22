package com.emmisolutions.emmimanager.service.jobs;

/**
 * Holds the list of all job names that can be scheduled
 */
public interface AllJobs {

    /**
     * Name for the hli cache clearing job bean
     */
    String CLEANUP_HLI_SEARCH_CACHE_JOB_BEAN_NAME = "cleanupHliSearchCache";

    /**
     * Group name for tasks that are necessary to cleanup or maintain common
     * data.
     */
    String HOUSEKEEPING_GROUP = "HOUSEKEEPING";

    /**
     * Group name for patient email job
     */
    String PATIENT_EMAIL_JOB_GROUP = "PATIENT_EMAIL";

    /**
     * Job name for scheduled program reminders, matches spring bean component name
     */
    String SCHEDULED_PROGRAM_REMINDER_EMAIL = "scheduledProgramReminderEmail";

}
