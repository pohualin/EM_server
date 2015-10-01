package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgramNote;

/**
 * Interface to scheduled program notes
 */
public interface ScheduledProgramNotesRepository {

    /**
     * Retrieve notes from a given scheduled program
     *
     * @param accessCode unique identifier
     * @return a ScheduledProgramNote containing the scheduled program's notes
     */
    ScheduledProgramNote find(String accessCode);

}
