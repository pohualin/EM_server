package com.emmisolutions.emmimanager.persistence.impl.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.model.ReferenceGroup_;

/**
 * This is the specification class that allows for filtering of ReferenceGroup
 * objects.
 */
@Component
public class ReferenceGroupSpecifications {

    /**
     * Method to generate Specification with active/inactive flag
     * 
     * @return
     */
    public Specification<ReferenceGroup> isActive() {
        return new Specification<ReferenceGroup>() {
            @Override
            public Predicate toPredicate(Root<ReferenceGroup> root,
                    CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get(ReferenceGroup_.active), true);
            }
        };
    }

}
