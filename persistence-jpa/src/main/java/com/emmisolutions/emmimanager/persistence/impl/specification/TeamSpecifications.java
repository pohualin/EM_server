package com.emmisolutions.emmimanager.persistence.impl.specification;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamSearchFilter;
import com.emmisolutions.emmimanager.model.Team_;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
public class TeamSpecifications {
	
	private TeamSpecifications(){		
	}
	
	/**
     * Case insensitive name anywhere match
     *
     * @param searchFilter to be found
     * @return the specification as a filter predicate
     */
    public static Specification<Team> hasNames(final TeamSearchFilter searchFilter) {
        return new Specification<Team>() {
            @Override
            public Predicate toPredicate(Root<Team> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (searchFilter != null && !CollectionUtils.isEmpty(searchFilter.getNames())) {
                    boolean addedANameFilter = false;
                    for (String name : searchFilter.getNames()) {
                        if (StringUtils.isNotBlank(name)) {
                            addedANameFilter = true;
                            predicates.add(cb.like(cb.lower(root.get(Team_.name)), "%" + name.toLowerCase() + "%"));
                        }
                    }
                    return addedANameFilter ? cb.or(predicates.toArray(new Predicate[predicates.size()])) : null;
                }
                return null;
            }
        };
    }

    /**
     * Ensures that the Team is in a particular status
     *
     * @param searchFilter used to find the status
     * @return the specification as a filter predicate
     */
    public static Specification<Team> isInStatus(final TeamSearchFilter searchFilter) {
        return new Specification<Team>() {
            @Override
            public Predicate toPredicate(Root<Team> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (searchFilter != null && TeamSearchFilter.StatusFilter.ALL != searchFilter.getStatus()) {
                    return cb.equal(root.get(Team_.active), TeamSearchFilter.StatusFilter.ACTIVE_ONLY.equals(searchFilter.getStatus()));
                }
                return null;
            }
        };
    }
    
    /**
     * Ensures that the team belongs to the client
     *
     * @param client to load, if null eager fetches all clients using this location
     * @return the specification
     */
    public static Specification<Team> usedBy(final Client client) {
        return new Specification<Team>() {
            @Override
            public Predicate toPredicate(Root<Team> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (client != null) {
                    return cb.equal(root.get(Team_.client),client);
                }
                return null;
            }
        };
    }
}
