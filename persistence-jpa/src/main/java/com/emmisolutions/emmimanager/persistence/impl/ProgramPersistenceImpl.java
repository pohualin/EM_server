package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.program.Program;
import com.emmisolutions.emmimanager.model.program.ProgramSearchFilter;
import com.emmisolutions.emmimanager.model.program.Specialty;
import com.emmisolutions.emmimanager.persistence.ProgramPersistence;
import com.emmisolutions.emmimanager.persistence.impl.specification.ProgramSpecifications;
import com.emmisolutions.emmimanager.persistence.repo.HliSearchRepository;
import com.emmisolutions.emmimanager.persistence.repo.ProgramRepository;
import com.emmisolutions.emmimanager.persistence.repo.ProgramSpecialtyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

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

    @Resource
    HliSearchRepository hliSearchRepository;

    @Override
    public Page<Program> find(ProgramSearchFilter filter, Pageable pageable) {
        Sort sort = pageable != null ? pageable.getSort() : null;
        boolean hasSearchTerms = filter != null && !CollectionUtils.isEmpty(filter.getTerms());
        if (hasSearchTerms && (sort == null || sort.equals(new Sort("type.weight")))) {
            // default of sorting by weight, if there are search terms and no other sorts are present
            sort = new Sort("type.weight", "hliProgram.weight");
        }
        return programRepository.findAll(where(programSpecifications
                        .hasSpecialties(filter))
                        .and(programSpecifications.matchesTerms(filter)),
                pageable != null ? new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort) :
                        new PageRequest(0, 10, sort));
    }

    @Override
    public Page<Specialty> findSpecialties(Pageable pageable) {
        return specialtyRepository.findAll(pageable != null ? pageable : new PageRequest(0, 10));
    }

}
