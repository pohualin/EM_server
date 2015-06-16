package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data Repo for ScheduledProgram objects
 */
public interface ScheduledProgramRepository extends JpaRepository<ScheduledProgram, Long>,
        JpaSpecificationExecutor<ScheduledProgram> {

    /**
     * Checks to see if the access code is already in use
     *
     * @param toCheck an access code to check
     * @return true if it already exists or false if it does not
     */
    ScheduledProgram findFirstByAccessCodeEquals(String toCheck);

    /**
     * Finds all scheduled programs for a given patient
     * @param patient to find by
     * @param page specification
     * @return a page of scheduled programs
     */
    Page<ScheduledProgram> findAllByPatient(Patient patient, Pageable page);

}
