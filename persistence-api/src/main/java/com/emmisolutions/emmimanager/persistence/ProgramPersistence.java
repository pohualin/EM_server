package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.program.Program;
import com.emmisolutions.emmimanager.model.program.ProgramSearchFilter;
import com.emmisolutions.emmimanager.model.program.Specialty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Program Persistence
 */
public interface ProgramPersistence {

    /**
     * Find programs
     *
     * @param filter   to narrow the results
     * @param pageable the page specification
     * @return a page of programs
     */
    Page<Program> find(ProgramSearchFilter filter, Pageable pageable);

    /**
     * Retrieves a page of Specialty objects
     *
     * @param page the page to load
     * @return a page of Specialty objects
     */
    Page<Specialty> findSpecialties(Pageable page);

    /**
     * Clears the hli search cache
     */
    int clearHliCache();
}
