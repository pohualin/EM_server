package com.emmisolutions.emmimanager.persistence.impl.specification;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.emmisolutions.emmimanager.model.Location_;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.LocationSearchFilter;
import com.emmisolutions.emmimanager.persistence.impl.helper.MatchingCriteriaBean;

/**
 * This is the specification class that allows for filtering of Location objects.
 */
@Component
public class LocationSpecifications {
    
	@Resource
	MatchingCriteriaBean matchCriteria;
	
    public LocationSpecifications() {
    }

    /**
     * Case insensitive name anywhere match
     *
     * @param searchFilter to be found
     * @return the specification as a filter predicate
     */
    public Specification<Location> hasNames(final LocationSearchFilter searchFilter) {
        return new Specification<Location>() {

            @Override
            public Predicate toPredicate(Root<Location> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                boolean addedANameFilter = false;
                List<Predicate> predicates = new ArrayList<>();
                if (searchFilter != null && !CollectionUtils.isEmpty(searchFilter.getNames())) {
                   
                    for (String name : searchFilter.getNames()) {
                        List<String> searchTerms = new ArrayList<>();
                        
                        for (String term: StringUtils.split(matchCriteria.normalizeName(name), " ")) {
                            if (!searchTerms.contains(term)){
                                searchTerms.add(term);
                            }
                        }
                        
                        for (String searchTerm: searchTerms){
                            if (StringUtils.isNotBlank(searchTerm)) {
                                addedANameFilter = true;
                                predicates.add(cb.like(cb.lower(root.get(Location_.name)), "%" + searchTerm.toLowerCase() + "%"));
                            }
                        }
                    }
                    return addedANameFilter ? cb.or(predicates.toArray(new Predicate[predicates.size()])) : null;
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
    public Specification<Location> isInStatus(final LocationSearchFilter searchFilter) {
        return new Specification<Location>() {
            @Override
            public Predicate toPredicate(Root<Location> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (searchFilter != null && LocationSearchFilter.StatusFilter.ALL != searchFilter.getStatus()) {
                    return cb.equal(root.get(Location_.active), LocationSearchFilter.StatusFilter.ACTIVE_ONLY.equals(searchFilter.getStatus()));
                }
                return null;
            }
        };
    }

    /**
     * Ensures that the location belongs to a client
     *
     * @param client to use
     * @return the specification
     */
    public Specification<Location> belongsTo(final Client client) {
        return new Specification<Location>() {
            @Override
            public Predicate toPredicate(Root<Location> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (client != null) {
                    return cb.equal(root.get(Location_.belongsTo), client);
                }
                return null;
            }
        };
    }

}
