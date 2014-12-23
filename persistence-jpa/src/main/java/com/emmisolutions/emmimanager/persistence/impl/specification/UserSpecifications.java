package com.emmisolutions.emmimanager.persistence.impl.specification;

import java.util.ArrayList;
import java.util.List;

import com.emmisolutions.emmimanager.model.UserSearchFilter;
import com.emmisolutions.emmimanager.model.user.admin.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.persistence.criteria.*;

import static com.emmisolutions.emmimanager.model.user.admin.UserAdminPermissionName.PERM_ADMIN_CONTRACT_OWNER;

/**
 * Specifications for a User.
 */
@Component
public class UserSpecifications {
	@Resource
	MatchingCriteriaBean matchCriteria;

	/**
	 * Method to generate Specification with search term
	 *
	 * @param filter
	 *            carriers search term
	 * @return Specification with search term
	 */
	public Specification<UserAdmin> hasNames(final UserSearchFilter filter) {
		return new Specification<UserAdmin>() {
			@Override
			public Predicate toPredicate(Root<UserAdmin> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (filter != null && StringUtils.isNotBlank(filter.getTerm())) {
					List<String> searchTerms = new ArrayList<>();
					String[] terms = StringUtils.split(
							matchCriteria.normalizeName(filter.getTerm()), " ");
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
								root.get(UserAdmin_.normalizedName), "%"
										+ searchTerm + "%"));
					}
					return cb.and(andClause.toArray(new Predicate[andClause
							.size()]));
				}
				return null;
			}
		};
	}

	/**
	 * Filter to ensure User is a contract owner
	 *
	 * @return the specification as a filter predicate
	 */
	public Specification<UserAdmin> isContractOwner() {

		return new Specification<UserAdmin>() {
			@Override
			public Predicate toPredicate(Root<UserAdmin> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Join<UserAdminRole, UserAdminPermission> permissions = root
						.join(UserAdmin_.roles)
						.join(UserAdminUserAdminRole_.userAdminRole)
						.join(UserAdminRole_.permissions);
				return cb.equal(permissions.get(UserAdminPermission_.name),
						PERM_ADMIN_CONTRACT_OWNER);
			}
		};
	}
}
