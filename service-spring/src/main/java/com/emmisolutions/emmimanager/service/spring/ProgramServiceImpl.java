package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.program.Program;
import com.emmisolutions.emmimanager.model.program.ProgramSearchFilter;
import com.emmisolutions.emmimanager.model.program.Specialty;
import com.emmisolutions.emmimanager.persistence.ProgramPersistence;
import com.emmisolutions.emmimanager.service.ProgramService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Program Service Implementation
 */
@Service
public class ProgramServiceImpl implements ProgramService {

    @Resource
    ProgramPersistence programPersistence;

    @Override
    @Transactional // not read-only due to temp table creation and insertion
    public Page<Program> find(ProgramSearchFilter filter, Pageable pageable) {
        return programPersistence.find(filter, pageable);
    }

    @Override
    public Page<Specialty> findSpecialties(Pageable page) {
        return programPersistence.findSpecialties(page);
    }

}
