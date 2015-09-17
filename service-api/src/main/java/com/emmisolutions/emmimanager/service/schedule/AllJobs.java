package com.emmisolutions.emmimanager.service.schedule;

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

}
