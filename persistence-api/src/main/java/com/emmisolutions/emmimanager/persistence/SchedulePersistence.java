package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;

/**
 * Persistence Layer for scheduling
 */
public interface SchedulePersistence {

    /**
     * Determines if the passed string is a unique access code
     *
     * @param toCheck with the db
     * @return true if the passed string is unique
     */
    boolean isAccessCodeUnique(String toCheck);

    /**
     * Saves a scheduled program
     *
     * @param toSave to save
     * @return the saved ScheduledProgram
     */
    ScheduledProgram save(ScheduledProgram toSave);

    /**
     * Loads the scheduled program
     *
     * @param scheduledProgram to load
     * @return the reloaded program
     */
    ScheduledProgram reload(ScheduledProgram scheduledProgram);
}
