package com.emmisolutions.emmimanager.persistence.impl.specification;

import com.emmisolutions.emmimanager.model.UserClientSearchFilter;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.UserClient_;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole_;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the specification class that allows for filtering of Client objects.
 */
@Component
public class UserClientSpecifications {

    @Resource
    MatchingCriteriaBean matchCriteria;

    /**
     * Method to generate Specification with search term
     *
     * @param filter carriers search term
     * @return Specification with search term
     */
    public Specification<UserClient> hasNames(
            final UserClientSearchFilter filter) {
        return new Specification<UserClient>() {
            @Override
            public Predicate toPredicate(Root<UserClient> root,
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
                                root.get(UserClient_.normalizedName), "%"
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
     * Method to generate Specification with clientId
     *
     * @param filter carries clientId
     * @return Specification with clientID
     */
    public Specification<UserClient> isClient(
            final UserClientSearchFilter filter) {
        return new Specification<UserClient>() {
            @Override
            public Predicate toPredicate(Root<UserClient> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (filter != null && filter.getClient() != null && filter.getClient().getId() != null) {
                    return cb.equal(root.get(UserClient_.client),
                            filter.getClient().getId());
                }
                return null;
            }
        };
    }

    /**
     * Find by login:
     * e.g where UserClient.login == userClient.getLogin()
     *
     * @param userClient to filter by
     * @return a specification or null
     */
    public Specification<UserClient> hasLogin(final UserClient userClient) {
        return new Specification<UserClient>() {
            @Override
            public Predicate toPredicate(Root<UserClient> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (userClient != null && StringUtils.isNotBlank(userClient.getLogin())) {
                    return cb.equal(root.get(UserClient_.login), userClient.getLogin().toLowerCase());
                }
                return null;
            }
        };
    }

    /**
     * Find by email:
     * e.g. where UserClient.email == userClient.getEmail()
     *
     * @param userClient to filter by
     * @return a specification or null
     */
    public Specification<UserClient> hasEmail(final UserClient userClient) {
        return new Specification<UserClient>() {
            @Override
            public Predicate toPredicate(Root<UserClient> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (userClient != null && StringUtils.isNotBlank(userClient.getEmail())) {
                    return cb.equal(root.get(UserClient_.email), userClient.getEmail().toLowerCase());
                }
                return null;
            }
        };
    }

    /**
     * Adds a not for the id of the passed userClient:
     * e.g. where UserClient.id != userClient.getId()
     *
     * @param userClient to use for the id to compare against
     * @return a specification if an id is present or null
     */
    public Specification<UserClient> isNot(final UserClient userClient) {
        return new Specification<UserClient>() {
            @Override
            public Predicate toPredicate(Root<UserClient> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (userClient != null && userClient.getId() != null) {
                    return cb.notEqual(root.get(UserClient_.id), userClient.getId());
                }
                return null;
            }
        };
    }

    /**
     * Ensures that the UserClient is in a particular status
     *
     * @param searchFilter used to find the status
     * @return the specification as a filter predicate
     */
    public Specification<UserClient> isInStatus(final UserClientSearchFilter searchFilter) {
        return new Specification<UserClient>() {
            @Override
            public Predicate toPredicate(Root<UserClient> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (searchFilter != null && UserClientSearchFilter.StatusFilter.ALL != searchFilter.getStatus()) {
                    return cb.equal(root.get(UserClient_.active), searchFilter.getStatus().equals(UserClientSearchFilter.StatusFilter.ACTIVE_ONLY));
                }
                return null;
            }
        };
    }

    /**
     * Ensures that the UserClient has a team role for the team in the search filter.
     *
     * @param searchFilter that contains the team
     * @return a specification if the team is present or null if it is not
     */
    public Specification<UserClient> hasRoleOnTeam(final UserClientSearchFilter searchFilter) {
        return new Specification<UserClient>() {
            @Override
            public Predicate toPredicate(Root<UserClient> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (searchFilter != null && searchFilter.getTeam() != null && searchFilter.getTeam().getId() != null) {
                    return cb.equal(root.join(UserClient_.teamRoles, JoinType.LEFT).get(UserClientUserClientTeamRole_.team),
                            searchFilter.getTeam().getId());
                }
                return null;
            }
        };
    }

}
