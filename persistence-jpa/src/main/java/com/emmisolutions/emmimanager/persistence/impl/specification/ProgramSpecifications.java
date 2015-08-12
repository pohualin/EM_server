package com.emmisolutions.emmimanager.persistence.impl.specification;

import com.emmisolutions.emmimanager.model.program.*;
import com.emmisolutions.emmimanager.model.program.hli.HliProgram;
import com.emmisolutions.emmimanager.persistence.repo.HliSearchRepository;
import org.springframework.data.domain.Pageable;
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
    HliSearchRepository hliSearchRepository;

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
    public Specification<Program> matchesTerms(final ProgramSearchFilter filter, final Pageable pageable) {
        return new Specification<Program>() {
            @Override
            public Predicate toPredicate(Root<Program> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (filter != null && !CollectionUtils.isEmpty(filter.getTerms())) {
                    // get the page of ids from full list
                    List<HliProgram> fullList = hliSearchRepository.find(filter);

                    // create temporary table


                    // insert full list into temporary table


                    return root.get(Program_.id).in();
                }
                return null;
            }
        };
    }
}
