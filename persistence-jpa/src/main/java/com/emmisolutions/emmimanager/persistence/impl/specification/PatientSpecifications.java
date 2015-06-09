package com.emmisolutions.emmimanager.persistence.impl.specification;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.PatientSearchFilter;
import com.emmisolutions.emmimanager.model.Patient_;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram_;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Patient filter specifications
 */
@Component
public class PatientSpecifications {

    @Resource
    MatchingCriteriaBean matchCriteria;

    @Resource
    ClientPersistence clientPersistence;

    /**
     * Adds a phone number filter if one is defined
     *
     * @param searchFilter for the phone attribute
     * @return a specification if there is a phone number in the filter or null
     */
    public Specification<Patient> withPhoneNumber(final PatientSearchFilter searchFilter) {
        return new Specification<Patient>() {
            @Override
            public Predicate toPredicate(Root<Patient> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (searchFilter != null && searchFilter.getPhone() != null) {
                    return cb.equal(root.get(Patient_.phone), searchFilter.getPhone());
                }
                return null;
            }
        };
    }

    /**
     * Adds an email specification if an email is defined in the search filter
     *
     * @param searchFilter for the email attribute
     * @return a specification if there is an email filter present or null
     */
    public Specification<Patient> withEmail(final PatientSearchFilter searchFilter) {
        return new Specification<Patient>() {
            @Override
            public Predicate toPredicate(Root<Patient> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (searchFilter != null && searchFilter.getEmail() != null) {
                    return cb.equal(root.get(Patient_.email), searchFilter.getEmail());
                }
                return null;
            }
        };
    }

    /**
     * Adds an access code specification if one is defined in the filter
     *
     * @param searchFilter for the access code
     * @return a specification or null
     */
    public Specification<Patient> withAccessCodes(final PatientSearchFilter searchFilter) {
        return new Specification<Patient>() {
            @Override
            public Predicate toPredicate(Root<Patient> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (searchFilter != null && searchFilter.getAccessCodes() != null){
                    List<Predicate> accessCodePredicates = new ArrayList<>();
                    for (String accessCode : searchFilter.getAccessCodes()) {
                        accessCodePredicates.add(cb.equal(root.join(Patient_.scheduledPrograms, JoinType.LEFT)
                                        .get(ScheduledProgram_.accessCode), accessCode));
                    }
                    query.distinct(accessCodePredicates.size() > 1);
                    return cb.or(accessCodePredicates.toArray(new Predicate[accessCodePredicates.size()]));
                }
                return null;
            }
        };
    }

    /**
     * Case insensitive name anywhere match
     *
     * @param searchFilter to be found
     * @return the specification as a filter predicate
     */
    public Specification<Patient> hasNames(final PatientSearchFilter searchFilter) {
        return new Specification<Patient>() {
            @Override
            public Predicate toPredicate(Root<Patient> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (searchFilter != null && !CollectionUtils.isEmpty(searchFilter.getNames())) {
                    for (String name : searchFilter.getNames()) {
                        List<String> searchTerms = new ArrayList<>();
                        String[] terms = StringUtils.split(matchCriteria.normalizeName(name), " ");
                        if (terms != null) {
                            for (String term : terms) {
                                if (!searchTerms.contains(term)) {
                                    searchTerms.add(term);
                                }
                            }
                        }
                        List<Predicate> andClause = new ArrayList<>();
                        for (String searchTerm : searchTerms) {
                            andClause.add(cb.like(root.get(Patient_.normalizedName), "%" + searchTerm + "%"));
                        }
                        predicates.add(cb.and(andClause.toArray(new Predicate[andClause.size()])));
                    }

                    return cb.or(predicates.toArray(new Predicate[predicates.size()]));
                }
                return null;
            }
        };
    }

    /**
     * Ensures that the patient belongs to a client
     *
     * @param filter to use to find the belongs to client
     * @return the specification
     */
    public Specification<Patient> belongsTo(final PatientSearchFilter filter) {
        return new Specification<Patient>() {
            @Override
            public Predicate toPredicate(Root<Patient> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Client belongsToClient = null;
                if (filter != null && filter.getClient() != null) {
                    belongsToClient = clientPersistence.reload(filter.getClient().getId());
                }
                if (belongsToClient != null) {
                    return cb.equal(root.get(Patient_.client), belongsToClient);
                }
                return null;
            }
        };
    }

}
