package com.emmisolutions.emmimanager.persistence.impl.specification;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientProvider;
import com.emmisolutions.emmimanager.model.ClientProvider_;
import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.ProviderSearchFilter;
import com.emmisolutions.emmimanager.model.Provider_;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import java.util.ArrayList;
import java.util.List;

/**
 * Provider filter specifications
 */
@Component
public class ProviderSpecifications {

    @Resource
    MatchingCriteriaBean matchCriteria;
    
    @Resource
    ClientPersistence clientPersistence;

	/**
     * Case insensitive name anywhere match
     *
     * @param searchFilter to be found
     * @return the specification as a filter predicate
     */
    public Specification<Provider> hasNames(final ProviderSearchFilter searchFilter) {
        return new Specification<Provider>() {
            @Override
            public Predicate toPredicate(Root<Provider> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (searchFilter != null && !CollectionUtils.isEmpty(searchFilter.getNames())) {
                for (String name: searchFilter.getNames()) {
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
                	for (String searchTerm: searchTerms){
                		andClause.add(cb.like(root.get(Provider_.normalizedName), "%" + searchTerm + "%"));
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
     * Ensures that the Provider is in a particular status
     *
     * @param searchFilter used to find the status
     * @return the specification as a filter predicate
     */
    public Specification<Provider> isInStatus(final ProviderSearchFilter searchFilter) {
        return new Specification<Provider>() {
            @Override
            public Predicate toPredicate(Root<Provider> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (searchFilter != null && ProviderSearchFilter.StatusFilter.ALL != searchFilter.getStatus()) {
                    return cb.equal(root.get(Provider_.active), ProviderSearchFilter.StatusFilter.ACTIVE_ONLY.equals(searchFilter.getStatus()));
                }
                return null;
            }
        };
    }
    
    /**
     * Ensures that the provider is not in use by the client on the filter
     * @param filter to find the client not to use
     * @return the specification
     */
    public Specification<Provider> notUsedBy(final ProviderSearchFilter filter) {
        return new Specification<Provider>() {
            @Override
            public Predicate toPredicate(Root<Provider> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Client notUsedByClient = null;
                if (filter != null && filter.getNotUsingThisClient() != null) {
                    notUsedByClient = clientPersistence.reload(filter.getNotUsingThisClient().getId());
                }
                if (notUsedByClient != null) {

                    Subquery<Provider> providerSubquery = query.subquery(Provider.class);
                    Root<ClientProvider> clientProviderRoot = providerSubquery.from(ClientProvider.class);
                    providerSubquery
                        .select(clientProviderRoot.get(ClientProvider_.provider))
                        .where(cb.equal(clientProviderRoot.get(ClientProvider_.client), notUsedByClient));
                    return cb.not(cb.in(root).value(providerSubquery));
                }
                return null;
            }
        };
    }
}
