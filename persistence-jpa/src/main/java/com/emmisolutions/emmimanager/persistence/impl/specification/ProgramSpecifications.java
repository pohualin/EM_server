package com.emmisolutions.emmimanager.persistence.impl.specification;

import com.emmisolutions.emmimanager.model.program.*;
import com.emmisolutions.emmimanager.model.program.hli.HliSearchRequest;
import com.emmisolutions.emmimanager.model.program.hli.HliSearchResponse;
import com.emmisolutions.emmimanager.model.program.hli.HliSearchResponse_;
import com.emmisolutions.emmimanager.persistence.repo.HliSearchRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Specifications holder for Program objects
 */
@Component
public class ProgramSpecifications {

    @Resource
    private HliSearchRepository hliSearchRepository;


    /**
     * Adds an OR clause for each specialty in the filter. This ensures that both
     * the Specialty and ProgramSpecialty are active
     *
     * @param filter containing Collection of 'active' specialties to look for
     * @return a Specification for the Program
     */
    public Specification<Program> hasSpecialties(final ProgramSearchFilter filter) {
        return new Specification<Program>() {
            @Override
            public Predicate toPredicate(Root<Program> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (filter != null && !CollectionUtils.isEmpty(filter.getSpecialties())) {
                    List<Predicate> predicates = new ArrayList<>();
                    for (Specialty specialty : filter.getSpecialties()) {
                        if (specialty.getId() != null) {
                            SetJoin<Program, ProgramSpecialty> join = root.join(Program_.programSpecialty);
                            predicates.add(
                                    cb.and(
                                            cb.isTrue(join.get(ProgramSpecialty_.active)),
                                            cb.and(cb.equal(join.get(ProgramSpecialty_.specialty), specialty),
                                                    cb.isTrue(join.join(ProgramSpecialty_.specialty).get(Specialty_.active)))
                                    )
                            );
                        }
                    }
                    return cb.or(predicates.toArray(new Predicate[predicates.size()]));
                }
                return null;
            }
        };
    }

    /**
     * Looks up program IDs from HLI and then uses those to narrow the program search
     *
     * @param filter that contains terms
     * @return a specification for program narrowing using ids found from terms
     * or null if there are no terms
     */
    public Specification<Program> matchesTerms(final ProgramSearchFilter filter) {
        return new Specification<Program>() {
            @Override
            public Predicate toPredicate(Root<Program> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (filter != null && !CollectionUtils.isEmpty(filter.getTerms())) {

                    // search HLI and retrieve (or create) the persistent search request
                    HliSearchRequest searchRequest = hliSearchRepository.find(filter);

                    // find all programs (on a search response) from the search request
                    Subquery<Program> programSubquery = query.subquery(Program.class);
                    Root<HliSearchResponse> searchResponseRoot = programSubquery.from(HliSearchResponse.class);
                    programSubquery
                            .select(searchResponseRoot.get(HliSearchResponse_.program))
                            .where(cb.equal(searchResponseRoot.get(HliSearchResponse_.hliSearchRequest), searchRequest));

                    // filter programs based upon the search response programs
                    return cb.in(root).value(programSubquery);
                }
                return null;
            }
        };
    }

}
