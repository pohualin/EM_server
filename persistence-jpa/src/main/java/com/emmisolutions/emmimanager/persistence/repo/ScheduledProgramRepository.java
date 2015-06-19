package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
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

}
