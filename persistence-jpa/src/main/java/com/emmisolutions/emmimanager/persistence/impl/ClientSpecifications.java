package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Client_;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This is the specification class that allows for filtering of Client objects.
 */
public class ClientSpecifications {

    /**
     * Case insensitive name anywhere match
     *
     * @param names to be found
     * @return the filter predicate
     */
    public static Specification<Client> hasNames(final Set<String> names) {
        return new Specification<Client>() {
            @Override
            public Predicate toPredicate(Root<Client> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!CollectionUtils.isEmpty(names)) {
                    for (String name : names) {
                        predicates.add(cb.like(cb.lower(root.get(Client_.name)), "%" + name.toLowerCase() + "%"));
                    }
                    return cb.or(predicates.toArray(new Predicate[predicates.size()]));
                }
                return null;
            }
        };
    }

    public static Specification<Client> isInStatus(final ClientPersistence.StatusFilter status){
        return new Specification<Client>() {
            @Override
            public Predicate toPredicate(Root<Client> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (status != null && ClientPersistence.StatusFilter.ALL != status){
                    return cb.equal(root.get(Client_.active), status.equals(ClientPersistence.StatusFilter.ACTIVE_ONLY));
                }
                return null;
            }
        };
    }

}
