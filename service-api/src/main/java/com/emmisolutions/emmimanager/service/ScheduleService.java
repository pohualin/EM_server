package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgramNote;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgramSearchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service layer for scheduling maintenance
 */
public interface ScheduleService {

    /**
     * Create a scheduled program
     *
     * @param toBeScheduled to be scheduled/saved, the viewByDate must be >= today
     * @return the persistent ScheduledProgram
     */
    ScheduledProgram schedule(ScheduledProgram toBeScheduled);

    /**
     * Reloads a scheduled program
     *
     * @param scheduledProgram to reload
     * @return the persistent ScheduledProgram
     */
    ScheduledProgram reload(ScheduledProgram scheduledProgram);

    /**
     * finds all scheduled programs for a given patient
     *
     * @param patient to find by
     * @param page    specification
     * @return a page of scheduled programs
     */
    Page<ScheduledProgram> findAllByPatient(Patient patient, Pageable page);

    /**
     * finds a page of scheduled programs by filter
     *
     * @param filter to AND together
     * @param page   specification
     * @return Page of ScheduledProgram objects matching filter
     */
    Page<ScheduledProgram> find(ScheduledProgramSearchFilter filter, Pageable page);

    /**
     * finds the notes for a given scheduled program
     *
     * @param scheduledProgram a scheduled program
     * @return a ScheduledProgramNote containing notes from a scheduled program
     */
    ScheduledProgramNote findNotes(ScheduledProgram scheduledProgram);

    /**
     * Update a scheduled program
     *
     * @param scheduledProgram to be updated
     * @return the persistent ScheduledProgram
     */
    ScheduledProgram update(ScheduledProgram scheduledProgram);

}
