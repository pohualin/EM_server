package com.emmisolutions.emmimanager.persistence.impl.specification;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.UserClientSearchFilter;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.UserClient_;

/**
 * This is the specification class that allows for filtering of Client objects.
 */
@Component
public class UserClientSpecifications {

    @Resource
    MatchingCriteriaBean matchCriteria;

    public Specification<UserClient> hasNames(
	    final UserClientSearchFilter filter) {
	return new Specification<UserClient>() {
	    @Override
	    public Predicate toPredicate(Root<UserClient> root,
		    CriteriaQuery<?> query, CriteriaBuilder cb) {
		List<Predicate> predicates = new ArrayList<>();
		if (filter != null
			&& !CollectionUtils.isEmpty(filter.getNames())) {
		    for (String name : filter.getNames()) {
			List<String> searchTerms = new ArrayList<>();
			String[] terms = StringUtils.split(
				matchCriteria.normalizeName(name), " ");
			if (terms != null) {
			    for (String term : terms) {
				if (!searchTerms.contains(term)) {
				    searchTerms.add(term);
				}
			    }
			}
			List<Predicate> andClause = new ArrayList<>();
			for (String searchTerm : searchTerms) {
			    andClause.add(cb.like(
				    root.get(UserClient_.normalizedName), "%"
					    + searchTerm + "%"));
			}
			predicates.add(cb.and(andClause
				.toArray(new Predicate[andClause.size()])));
		    }
		    return cb.or(predicates.toArray(new Predicate[predicates
			    .size()]));
		}
		return null;
	    }
	};
    }

    public Specification<UserClient> isClient(final Client client) {
	return new Specification<UserClient>() {
	    @Override
	    public Predicate toPredicate(Root<UserClient> root,
		    CriteriaQuery<?> query, CriteriaBuilder cb) {
		if (client != null && client.getId() != null) {
		    return cb.equal(root.get(UserClient_.client),
			    client.getId());
		}
		return null;
	    }
	};
    }

}
