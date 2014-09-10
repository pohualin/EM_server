package com.emmisolutions.emmimanager.persistence.impl.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.model.TagSearchFilter;

import com.emmisolutions.emmimanager.model.Tag_;
/**
 * This is the specification class that allows for filtering of Tag objects.
 */
public class TagSpecifications {

    private TagSpecifications(){}

    /**
     *
     * match on groupId within the provided TagSearchFilter
     * @param TagSearchFilter
     * @return the specification as a filter predicate
     */
    public static Specification<Tag> byGroupId(final TagSearchFilter searchFilter) {
        return new Specification<Tag>() {
            @Override
            public Predicate toPredicate(Root<Tag> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (searchFilter != null && searchFilter.getGroupId()!=null) {
                    return cb.equal(root.get(Tag.group), searchFilter.getGroupId());
                }
                return null;
            }
        };
    }
}
