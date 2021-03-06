package com.emmisolutions.emmimanager.persistence.impl.specification;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the specification class that allows for filtering of Location objects.
 */
@Component
public class LocationSpecifications {

    @Resource
    MatchingCriteriaBean matchCriteria;

    @Resource
    ClientPersistence clientPersistence;

    /**
     * Case insensitive name anywhere match. This searches the
     * normalized name field if the search filter has any names
     * on it. For example,
     *
     * SearchFilter.names == {"big brown", "car"}
     * The spec based upon this would be:
     * (normalizedName like %big% AND normalizedName like %brown%) OR normalizedName like %car%
     *
     * @param searchFilter inspects the names property
     * @return the specification as a filter predicate
     */
    public Specification<Location> hasNames(final LocationSearchFilter searchFilter) {
        return new Specification<Location>() {

            @Override
            public Predicate toPredicate(Root<Location> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
                            andClause.add(cb.like(root.get(Location_.normalizedName), "%" + searchTerm + "%"));
                        }
                        // AND all of the parts of a search term delimited by space
                        predicates.add(cb.and(andClause.toArray(new Predicate[andClause.size()])));
                    }
                    // OR all of the search names together
                    return cb.or(predicates.toArray(new Predicate[predicates.size()]));
                }
                return null;
            }
        };
    }

    /**
     * Ensures that the Location is in a particular status
     *
     * @param searchFilter used to find the status
     * @return the specification as a filter predicate
     */
    public Specification<Location> isInStatus(final LocationSearchFilter searchFilter) {
        return new Specification<Location>() {
            @Override
            public Predicate toPredicate(Root<Location> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (searchFilter != null && LocationSearchFilter.StatusFilter.ALL != searchFilter.getStatus()) {
                    return cb.equal(root.get(Location_.active), LocationSearchFilter.StatusFilter.ACTIVE_ONLY.equals(searchFilter.getStatus()));
                }
                return null;
            }
        };
    }

    /**
     * Ensures that the location belongs to a client
     *
     * @param filter to use to find the belongs to client
     * @return the specification
     */
    public Specification<Location> belongsTo(final LocationSearchFilter filter) {
        return new Specification<Location>() {
            @Override
            public Predicate toPredicate(Root<Location> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Client belongsToClient = null;
                if (filter != null && filter.getBelongsToClient() != null) {
                    belongsToClient = clientPersistence.reload(filter.getBelongsToClient());
                }
                if (belongsToClient != null) {
                    return cb.equal(root.get(Location_.belongsTo), belongsToClient);
                }
                return null;
            }
        };
    }

    /**
     * Ensures that the location is not in use by the client on the filter
     *
     * @param filter to find the client not to use
     * @return the specification
     */
    public Specification<Location> notUsedBy(final LocationSearchFilter filter) {
        return new Specification<Location>() {
            @Override
            public Predicate toPredicate(Root<Location> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Client notUsedByClient = null;
                if (filter != null && filter.getNotUsingThisClient() != null) {
                    notUsedByClient = clientPersistence.reload(filter.getNotUsingThisClient());
                }
                if (notUsedByClient != null) {

                	Subquery<Location> locationSubquery = query.subquery(Location.class);
                	Root<ClientLocation> clientLocationRoot = locationSubquery.from(ClientLocation.class);
                	locationSubquery
                	    .select(clientLocationRoot.get(ClientLocation_.location))
                	    .where(cb.equal(clientLocationRoot.get(ClientLocation_.client), notUsedByClient));
                	return cb.not(cb.in(root).value(locationSubquery));
                }
                return null;
            }
        };
    }
}
