package com.emmisolutions.emmimanager.persistence.impl.specification;

import com.emmisolutions.emmimanager.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
}
