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

    /**
     * This method should not be called twice within the same transaction when there are search
     * terms present. This is because of how the results of the search terms are pushed into
     * a dynamically created temporary table. The temp table gets created destroyed at transactional
     * boundaries. Essentially you shouldn't call this method two times without the temp tables
     * being destroyed. Currently H2 and PostgreSQL destroy the tables on the transaction commit,
     * SQLServer seems to destroy them at the end of the current 'session' which I think means
     * when the connection goes back into the pool.
     * <p/>
     * Unless there is some new weird requirement, our current flow will not ever call this method twice
     * within the same transaction. E.g. if we need to fetch multiple pages of programs, the angular-client
     * would need to ask for them separately.
     *
     * @param filter   to narrow the results
     * @param pageable the page specification
     * @return a page of programs
     */
    @Override
    public Page<Program> find(ProgramSearchFilter filter, Pageable pageable) {
        Sort sort = pageable != null ? pageable.getSort() : null;
        boolean hasSearchTerms = filter != null && !CollectionUtils.isEmpty(filter.getTerms());
        if (hasSearchTerms && sort == null) {
            // default of sorting by weight, if there are search terms and no other sorts are present
            sort = new Sort("hliProgram.weight");
        }
        try {
            return programRepository.findAll(where(programSpecifications
                            .hasSpecialties(filter))
                            .and(programSpecifications.matchesTerms(filter)),
                    pageable != null ? new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort) :
                            new PageRequest(0, 10, sort));
        } finally {
            if (hasSearchTerms) {
                programSpecifications.resetThread();
            }
        }
    }

    @Override
    public Page<Specialty> findSpecialties(Pageable pageable) {
        return specialtyRepository.findAll(pageable != null ? pageable : new PageRequest(0, 10));
    }

}
