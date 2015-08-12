package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.program.ProgramSearchFilter;
import com.emmisolutions.emmimanager.model.program.hli.HliProgram;

import java.util.List;

public interface HliSearchRepository {

    /**
     * Query the HLI REST service
     *
     * @param filter used to narrow the query
     * @return List of program ids
     */
    List<HliProgram> find(ProgramSearchFilter filter);
}
