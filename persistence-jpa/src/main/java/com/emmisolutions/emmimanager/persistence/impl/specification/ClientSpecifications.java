package com.emmisolutions.emmimanager.persistence.impl.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientSearchFilter;
import com.emmisolutions.emmimanager.model.Client_;

/**
 * This is the specification class that allows for filtering of Client objects.
 */
public class ClientSpecifications {

    private ClientSpecifications(){}

    /**
     * Case insensitive name anywhere match
     *
     * @param searchFilter to be found
     * @return the specification as a filter predicate
     */
    public static Specification<Client> hasNames(final ClientSearchFilter searchFilter) {
        return new Specification<Client>() {
            @Override
            public Predicate toPredicate(Root<Client> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (searchFilter != null && !CollectionUtils.isEmpty(searchFilter.getNames())) {
                    for (String name : searchFilter.getNames()) {
                        predicates.add(cb.like(cb.lower(root.get(Client_.name)), "%" + name.toLowerCase() + "%"));
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
    public static Specification<Client> isInStatus(final ClientSearchFilter searchFilter) {
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
