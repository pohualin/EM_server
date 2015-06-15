package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    /**
     * finds all scheduled programs for a given patient
     * @param patient to find by
     * @param page specification
     * @return a page of scheduled programs
     */
    Page<ScheduledProgram> findAllByPatient(Patient patient, Pageable page);

}
