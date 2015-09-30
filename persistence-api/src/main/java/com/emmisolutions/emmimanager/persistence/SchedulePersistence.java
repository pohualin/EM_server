package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgramNote;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgramSearchFilter;
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
     * Finds a page of scheduled programs based upon the passed filter
     *
     * @param searchFilter to find programs
     * @param page         specification
     * @return page of ScheduledProgram objects
     */
    Page<ScheduledProgram> find(ScheduledProgramSearchFilter searchFilter, Pageable page);

    /**
     * Find notes from a scheduled program
     *
     * @param accessCode unique identifier
     * @return a ScheduledProgrmNote containing program notes
     */
    ScheduledProgramNote findNotes(String accessCode);

}
