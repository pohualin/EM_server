package com.emmisolutions.emmimanager.persistence.impl.specification;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.Patient_;
import com.emmisolutions.emmimanager.model.Team_;
import com.emmisolutions.emmimanager.model.schedule.Encounter_;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgramSearchFilter;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram_;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * ScheduledProgram finder specifications.
 */
@Component
public class ScheduledProgramSpecifications {

    /**
     * Spec based upon ID in filter
     *
     * @param filter to get id
     * @return a Specification or null
     */
    public Specification<ScheduledProgram> id(final ScheduledProgramSearchFilter filter) {

        return new Specification<ScheduledProgram>() {
            @Override
            public Predicate toPredicate(Root<ScheduledProgram> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (filter != null && filter.id() != null) {
                    return cb.equal(root.get(ScheduledProgram_.id), filter.id());
                }
                return null;
            }
        };
    }

    /**
     * Spec based upon team in filter
     *
     * @param filter to get team
     * @return a Specification or null
     */
    public Specification<ScheduledProgram> team(final ScheduledProgramSearchFilter filter) {

        return new Specification<ScheduledProgram>() {
            @Override
            public Predicate toPredicate(Root<ScheduledProgram> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (filter != null && filter.team() != null) {
                    return cb.equal(root.join(ScheduledProgram_.team)
                            .get(Team_.id), filter.team().getId());
                }
                return null;
            }
        };
    }
    
    /**
     * Spec based upon encounter in filter
     *
     * @param filter to get encounter
     * @return a Specification or null
     */
    public Specification<ScheduledProgram> encounter(final ScheduledProgramSearchFilter filter) {

        return new Specification<ScheduledProgram>() {
            @Override
            public Predicate toPredicate(Root<ScheduledProgram> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (filter != null && filter.encounter() != null) {
                    return cb.equal(root.join(ScheduledProgram_.encounter)
                            .get(Encounter_.id), filter.encounter().getId());
                }
                return null;
            }
        };
    }

    /**
     * Spec based upon patients in filter
     *
     * @param filter to get patients
     * @return a Specification or null
     */
    public Specification<ScheduledProgram> patients(final ScheduledProgramSearchFilter filter) {
        return new Specification<ScheduledProgram>() {
            @Override
            public Predicate toPredicate(Root<ScheduledProgram> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (filter != null && filter.patients() != null) {
                    List<Predicate> patientPredicates = new ArrayList<>();
                    for (Patient patient : filter.patients()) {
                        patientPredicates.add(cb.equal(root.join(ScheduledProgram_.patient).get(Patient_.id), patient.getId()));
                    }
                    return cb.or(patientPredicates.toArray(new Predicate[patientPredicates.size()]));
                }
                return null;
            }
        };
    }

    /**
     * Spec based upon access codes in filter
     *
     * @param filter to get access codes
     * @return a Specification or null
     */
    public Specification<ScheduledProgram> accessCodes(final ScheduledProgramSearchFilter filter) {
        return new Specification<ScheduledProgram>() {
            @Override
            public Predicate toPredicate(Root<ScheduledProgram> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (filter != null && filter.accessCodes() != null) {
                    List<Predicate> accessCodePredicates = new ArrayList<>();
                    for (String accessCode : filter.accessCodes()) {
                        accessCodePredicates.add(cb.equal(root.get(ScheduledProgram_.accessCode), accessCode));
                    }
                    return cb.or(accessCodePredicates.toArray(new Predicate[accessCodePredicates.size()]));
                }
                return null;
            }
        };
    }

    /**
     * Spec based upon whether or not to show expired scheduled programs
     *
     * @param filter to determine expired
     * @return a Specification or null
     */
    public Specification<ScheduledProgram> expired(final ScheduledProgramSearchFilter filter) {
        return new Specification<ScheduledProgram>() {
            @Override
            public Predicate toPredicate(Root<ScheduledProgram> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (filter != null && !filter.includeExpired()) {
                    return cb.greaterThanOrEqualTo(root.get(ScheduledProgram_.viewByDate), LocalDate.now(DateTimeZone.UTC));
                }
                return null;
            }
        };
    }

}
