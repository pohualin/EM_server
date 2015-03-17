package com.emmisolutions.emmimanager.persistence.impl.specification;

import com.emmisolutions.emmimanager.model.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * This is the specification class that allows for filtering of Group objects.
 */
@Component
public class GroupSpecifications {

    /**
     * match on clientId within the provided GroupSearchFilter
     *
     * @param searchFilter GroupSearchFilter
     * @return the specification as a filter predicate
     */
    public Specification<Group> byClientId(final GroupSearchFilter searchFilter) {
        return new Specification<Group>() {
            @Override
            public Predicate toPredicate(Root<Group> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (searchFilter != null && searchFilter.getClientId() != null) {
                    return cb.equal(root.get(Group_.client), searchFilter.getClientId());
                }
                return null;
            }
        };
    }

    /**
     * find all Groups that use a particular reference group
     *
     * @param referenceGroup which points to a ReferenceGroupType that is used by a group
     * @return the specification if the reference group is persistent
     */
    public Specification<Group> using(final ReferenceGroup referenceGroup) {
        return new Specification<Group>() {
            @Override
            public Predicate toPredicate(Root<Group> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (referenceGroup != null &&
                        referenceGroup.getType() != null &&
                        referenceGroup.getType().getId() != null) {
                    return cb.equal(root.get(Group_.type).get(ReferenceGroupType_.id),
                            referenceGroup.getType().getId());
                }
                return null;
            }
        };
    }
}
