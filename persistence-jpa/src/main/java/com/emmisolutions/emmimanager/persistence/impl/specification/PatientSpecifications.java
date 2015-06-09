package com.emmisolutions.emmimanager.persistence.impl.specification;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram_;
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
                if (searchFilter != null && searchFilter.phones() != null) {
                    List<Predicate> phonePredicates = new ArrayList<>();
                    for (String phone : searchFilter.phones()) {
                        phonePredicates.add(cb.equal(root.get(Patient_.phone), phone));
                    }
                    return cb.or(phonePredicates.toArray(new Predicate[phonePredicates.size()]));
                }
                return null;
            }
        };
    }

    /**
     * Adds an email specification if an email is defined in the search filter.
     * If multiple emails are used they are OR'd together.
     *
     * @param searchFilter for the email attribute
     * @return a specification if there is an email filter present or null
     */
    public Specification<Patient> withEmail(final PatientSearchFilter searchFilter) {
        return new Specification<Patient>() {
            @Override
            public Predicate toPredicate(Root<Patient> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (searchFilter != null && searchFilter.emails() != null) {
                    List<Predicate> emailPredicates = new ArrayList<>();
                    for (String email : searchFilter.emails()) {
                        emailPredicates.add(cb.equal(root.get(Patient_.email), email));
                    }
                    return cb.or(emailPredicates.toArray(new Predicate[emailPredicates.size()]));
                }
                return null;
            }
        };
    }

    /**
     * Adds an access code specification if one is defined in the filter. If multiple
     * access codes are used they are OR'd together.
     *
     * @param searchFilter for the access code
     * @return a specification or null
     */
    public Specification<Patient> withAccessCodes(final PatientSearchFilter searchFilter) {
        return new Specification<Patient>() {
            @Override
            public Predicate toPredicate(Root<Patient> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (searchFilter != null && searchFilter.accessCodes() != null) {
                    List<Predicate> accessCodePredicates = new ArrayList<>();
                    for (String accessCode : searchFilter.accessCodes()) {
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
                if (searchFilter != null && !CollectionUtils.isEmpty(searchFilter.names())) {
                    for (String name : searchFilter.names()) {
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
     * Filter patients by teams on which a scheduled program has occurred.
     *
     * @param searchFilter to look up the teams
     * @return a specification ORing together any teams found in the filter
     */
    public Specification<Patient> scheduledForTeams(final PatientSearchFilter searchFilter) {
        return new Specification<Patient>() {
            @Override
            public Predicate toPredicate(Root<Patient> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (searchFilter != null && searchFilter.teams() != null) {
                    List<Predicate> teamPredicates = new ArrayList<>();
                    for (Team team : searchFilter.teams()) {
                        if (team.getId() != null) {
                            teamPredicates.add(cb.equal(root.join(Patient_.scheduledPrograms, JoinType.LEFT)
                                    .join(ScheduledProgram_.team).get(Team_.id), team.getId()));
                        }
                    }
                    if (!teamPredicates.isEmpty()) {
                        query.distinct(teamPredicates.size() > 1);
                        return cb.or(teamPredicates.toArray(new Predicate[teamPredicates.size()]));
                    }
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
                if (filter != null && filter.client() != null && filter.client().getId() != null) {
                    return cb.equal(root.join(Patient_.client).get(Client_.id), filter.client().getId());
                }
                return null;
            }
        };
    }

}
