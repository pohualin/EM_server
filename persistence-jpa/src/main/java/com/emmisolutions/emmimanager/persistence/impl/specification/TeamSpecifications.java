package com.emmisolutions.emmimanager.persistence.impl.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamSearchFilter;
import com.emmisolutions.emmimanager.model.Team_;
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
                for (String name: searchFilter.getNames()) {
                	List<String> searchTerms = new ArrayList<>();
                	
                	for (String term: StringUtils.split(normalizeName(name), " ")) {
                		if (!searchTerms.contains(term)){
                			searchTerms.add(term);
                		}
                	}
                	
                	List<Predicate> andClause = new ArrayList<>();
                	for (String searchTerm: searchTerms){
                		andClause.add(cb.like(root.get(Team_.normalizedTeamName), "%" + searchTerm + "%"));
                	}
                	predicates.add(cb.and(andClause.toArray(new Predicate[andClause.size()])));
                }
                	
                return cb.or(predicates.toArray(new Predicate[predicates.size()]));
                }
                return null;
            }
        };
    }

    private static String normalizeName(String name){
    	String normalizedName = StringUtils.trimToEmpty(StringUtils.lowerCase(name));
    	if (StringUtils.isNotBlank(normalizedName)){
    		normalizedName = normalizedName.replaceAll("[^a-z0-9 ]*", "");
    	}
    	return normalizedName;
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
     * @param team to load for a client, if null eager fetches all teams
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
