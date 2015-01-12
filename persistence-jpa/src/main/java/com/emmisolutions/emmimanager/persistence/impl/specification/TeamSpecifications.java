package com.emmisolutions.emmimanager.persistence.impl.specification;

import com.emmisolutions.emmimanager.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Specifications for the Team object
 */
@Component
public class TeamSpecifications {

    @Resource
    MatchingCriteriaBean matchCriteria;

    /**
     * Case insensitive name anywhere match
     *
     * @param searchFilter to be found
     * @return the specification as a filter predicate
     */
    public Specification<Team> hasNames(final TeamSearchFilter searchFilter) {
        return new Specification<Team>() {
            @Override
            public Predicate toPredicate(Root<Team> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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

    /**
     * Ensures that the Team is in a particular status
     *
     * @param searchFilter used to find the status
     * @return the specification as a filter predicate
     */
    public Specification<Team> isInStatus(final TeamSearchFilter searchFilter) {
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
     * @param searchFilter uses the clientId
     * @return the specification
     */
    public Specification<Team> usedByClient(final TeamSearchFilter searchFilter) {
        return new Specification<Team>() {
            @Override
            public Predicate toPredicate(Root<Team> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (searchFilter != null && searchFilter.getClientId() != null) {
                    return cb.equal(root.get(Team_.client), searchFilter.getClientId());
                }
                return null;
            }
        };
    }
    
    /**
     * Ensures that the team has the passed in team tag
     * 
     * @param searchFilter
     *            to filter
     * @return the specification
     */
    public Specification<Team> hasTeamTag(final TeamSearchFilter searchFilter) {
        return new Specification<Team>() {
            @Override
            public Predicate toPredicate(Root<Team> root,
                    CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (searchFilter != null && searchFilter.getTag() != null) {
                    return cb.equal(root.join(Team_.teamTags).get(TeamTag_.tag)
                            .get(Tag_.id), searchFilter.getTag().getId());
                }
                return null;
            }
        };
    }

    /**
     * Gets Teams with no teamtags associated
     *
     * @param searchFilter used to find the status
     * @return the specification as a filter predicate
     */
    public Specification<Team> hasNoTeamTags(final TeamSearchFilter searchFilter) {
        return new Specification<Team>() {
            @Override
            public Predicate toPredicate(Root<Team> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (searchFilter != null && searchFilter.getTeamTagType() != null) {
                    Expression<Set<TeamTag>> teamTags = root.get(Team_.teamTags);
                    if (TeamSearchFilter.TeamTagType.TAGGED_ONLY.equals(searchFilter.getTeamTagType())) {
                        return cb.isNotEmpty(teamTags);
                    } else {
                        return cb.isEmpty(teamTags);
                    }
                }
                return null;
            }
        };
    }
}
