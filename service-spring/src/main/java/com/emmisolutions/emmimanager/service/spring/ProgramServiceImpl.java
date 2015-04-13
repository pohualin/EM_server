package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.program.Program;
import com.emmisolutions.emmimanager.persistence.ProgramPersistence;
import com.emmisolutions.emmimanager.service.ProgramService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Program Service Implementation
 */
@Service
public class ProgramServiceImpl implements ProgramService {

    @Resource
    ProgramPersistence programPersistence;

    @Override
    public Page<Program> find(Pageable pageable) {
        return programPersistence.find(pageable);
    }

}
