package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.program.Program;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Program Service
 */
public interface ProgramService {

    /**
     * Find programs
     *
     * @param pageable the page specification
     * @return a page of programs
     */
    Page<Program> find(Pageable pageable);

}
