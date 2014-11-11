package com.emmisolutions.emmimanager.persistence.impl.specification;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientSearchFilter;
import com.emmisolutions.emmimanager.model.Client_;
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
 * This is the specification class that allows for filtering of Client objects.
 */
@Component
public class ClientSpecifications {

    @Resource
    MatchingCriteriaBean matchCriteria;

    /**
     * EM-12: Client types multiple words, separated only by spaces (no other delimiters needed),
     * all client names containing all of those words (in any order) should show up.
     *
     * @param searchFilter to be found
     * @return the specification as a filter predicate
     */
    public Specification<Client> hasNames(final ClientSearchFilter searchFilter) {
        return new Specification<Client>() {
            @Override
            public Predicate toPredicate(Root<Client> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (searchFilter != null && !CollectionUtils.isEmpty(searchFilter.getNames())) {
                    for (String name : searchFilter.getNames()) {
                        List<String> searchTerms = new ArrayList<>();

                        // filter out duplicate terms
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
                            andClause.add(cb.like(root.get(Client_.normalizedName), "%" + searchTerm + "%"));
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
     * Ensures that the Client is in a particular status
     *
     * @param searchFilter used to find the status
     * @return the specification as a filter predicate
     */
    public Specification<Client> isInStatus(final ClientSearchFilter searchFilter) {
        return new Specification<Client>() {
            @Override
            public Predicate toPredicate(Root<Client> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (searchFilter != null && ClientSearchFilter.StatusFilter.ALL != searchFilter.getStatus()) {
                    return cb.equal(root.get(Client_.active), searchFilter.getStatus().equals(ClientSearchFilter.StatusFilter.ACTIVE_ONLY));
                }
                return null;
            }
        };
    }

}
