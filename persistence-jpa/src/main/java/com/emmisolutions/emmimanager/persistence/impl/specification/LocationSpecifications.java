package com.emmisolutions.emmimanager.persistence.impl.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.LocationSearchFilter;
import com.emmisolutions.emmimanager.model.Location_;

/**
 * This is the specification class that allows for filtering of Location objects.
 */
public class LocationSpecifications {

    private LocationSpecifications(){}

    /**
     * Case insensitive name anywhere match
     *
     * @param names to be found
     * @return the specification as a filter predicate
     */
    public static Specification<Location> hasNames(final LocationSearchFilter searchFilter) {
        return new Specification<Location>() {
            @Override
            public Predicate toPredicate(Root<Location> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (searchFilter != null && !CollectionUtils.isEmpty(searchFilter.getNames())) {
                    for (String name : searchFilter.getNames()) {
                        predicates.add(cb.like(cb.lower(root.get(Location_.name)), "%" + name.toLowerCase() + "%"));
                    }
                    return cb.or(predicates.toArray(new Predicate[predicates.size()]));
                }
                return null;
            }
        };
    }

    /**
     * Ensures that the Location is in a particular status
     *
     * @param searchFilter used to find the status
     * @return the specification as a filter predicate
     */
    public static Specification<Location> isInStatus(final LocationSearchFilter searchFilter) {
        return new Specification<Location>() {
            @Override
            public Predicate toPredicate(Root<Location> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (searchFilter != null && LocationSearchFilter.StatusFilter.ALL != searchFilter.getStatus()) {
                    return cb.equal(root.get(Location_.active), searchFilter.getStatus().equals(LocationSearchFilter.StatusFilter.ACTIVE_ONLY));
                }
                return null;
            }
        };
    }

}
