package com.emmisolutions.emmimanager.persistence.impl.specification;

import com.emmisolutions.emmimanager.model.TeamTag;
import com.emmisolutions.emmimanager.model.TeamTagSearchFilter;
import com.emmisolutions.emmimanager.model.TeamTag_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class TeamTagSpecifications {
    private TeamTagSpecifications(){}

    /**
     *
     * match on tagId within the provided TeamTagSearchFilter
     * @param searchFilter
     * @return the specification as a filter predicate
     */
    public static Specification<TeamTag> byTagId(final TeamTagSearchFilter searchFilter) {
        return new Specification<TeamTag>() {
            @Override
            public javax.persistence.criteria.Predicate toPredicate(Root<TeamTag> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (searchFilter != null && searchFilter.getTagId()!=null) {
                    return cb.equal(root.get(TeamTag_.tag), searchFilter.getTagId());
                }
                return null;
            }
        };
    }
}
