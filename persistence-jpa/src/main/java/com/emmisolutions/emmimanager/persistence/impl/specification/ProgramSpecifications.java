package com.emmisolutions.emmimanager.persistence.impl.specification;

import com.emmisolutions.emmimanager.model.program.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Specifications holder for Program objects
 */
@Component
public class ProgramSpecifications {

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
}
