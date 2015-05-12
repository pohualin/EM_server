package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.program.Program;
import com.emmisolutions.emmimanager.model.program.ProgramSearchFilter;
import com.emmisolutions.emmimanager.model.program.Specialty;
import com.emmisolutions.emmimanager.persistence.ProgramPersistence;
import com.emmisolutions.emmimanager.persistence.impl.specification.ProgramSpecifications;
import com.emmisolutions.emmimanager.persistence.repo.ProgramRepository;
import com.emmisolutions.emmimanager.persistence.repo.ProgramSpecialtyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * Program Persistence Implementation
 */
@Repository
public class ProgramPersistenceImpl implements ProgramPersistence {

    @Resource
    ProgramRepository programRepository;

    @Resource
    ProgramSpecialtyRepository specialtyRepository;

    @Resource
    ProgramSpecifications programSpecifications;

    @Override
    public Page<Program> find(ProgramSearchFilter filter, Pageable pageable) {
        return programRepository.findAll(where(programSpecifications.hasSpecialties(filter)),
                pageable != null ? pageable : new PageRequest(0, 10));
    }

    @Override
    public Page<Specialty> findSpecialties(Pageable pageable) {
        return specialtyRepository.findAll(pageable != null ? pageable : new PageRequest(0, 10));
    }

}
