package com.emmisolutions.emmimanager.persistence.impl.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.emmisolutions.emmimanager.model.Group;
import com.emmisolutions.emmimanager.model.GroupSearchFilter;
import com.emmisolutions.emmimanager.model.Group_;

/**
 * This is the specification class that allows for filtering of Group objects.
 */
public class GroupSpecifications {

    private GroupSpecifications(){}

    /**
     * match on clientId within the provided GroupSearchFilter
     * 
     * @param GroupSearchFilter
     * @return the specification as a filter predicate
     */
    public static Specification<Group> byClientId(final GroupSearchFilter searchFilter) {
        return new Specification<Group>() {
            @Override
            public Predicate toPredicate(Root<Group> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (searchFilter != null && searchFilter.getClientId()!=null) {
                    return cb.equal(root.get(Group_.client), searchFilter.getClientId());
                }
                return null;
            }
        };
    }
}
