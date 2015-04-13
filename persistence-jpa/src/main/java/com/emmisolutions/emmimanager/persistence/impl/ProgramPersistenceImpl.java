package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.program.Program;
import com.emmisolutions.emmimanager.persistence.ProgramPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ProgramRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Program Persistence Implementation
 */
@Repository
public class ProgramPersistenceImpl implements ProgramPersistence {

    @Resource
    ProgramRepository programRepository;

    @Override
    public Page<Program> find(Pageable pageable) {
        return programRepository.findAll(pageable != null ? pageable : new PageRequest(0, 10));
    }

}
