package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;

/**
 * Service layer for scheduling maintenance
 */
public interface ScheduleService {

    /**
     * Create a scheduled program
     * @param toBeScheduled to be scheduled/saved
     * @return the persistent ScheduledProgram
     */
    ScheduledProgram schedule(ScheduledProgram toBeScheduled);
}
