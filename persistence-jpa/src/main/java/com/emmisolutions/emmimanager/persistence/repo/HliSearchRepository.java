package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.program.ProgramSearchFilter;
import com.emmisolutions.emmimanager.model.program.hli.HliProgram;

import java.util.Set;

public interface HliSearchRepository {

    /**
     * Query the HLI REST service
     *
     * @param filter used to narrow the query
     * @return List of program ids
     */
    Set<HliProgram> find(ProgramSearchFilter filter);
}
