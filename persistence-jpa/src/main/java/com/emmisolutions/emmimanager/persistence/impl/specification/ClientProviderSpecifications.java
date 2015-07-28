package com.emmisolutions.emmimanager.persistence.impl.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.ClientProvider;
import com.emmisolutions.emmimanager.model.ClientProvider_;
import com.emmisolutions.emmimanager.model.Provider_;

/**
 * This is the specification class that allows for filtering of Client objects.
 */
@Component
public class ClientProviderSpecifications {

    /**
     * Method to generate Specification with clientId. This also adds the
     * DISTINCT to the query so that UserClient objects within a client will not
     * repeat if other filters are used across joins.
     *
     * @param filter
     *            carries clientId
     * @return Specification with clientID
     */
    public Specification<ClientProvider> isClient(final Long clientId) {
        return new Specification<ClientProvider>() {
            @Override
            public Predicate toPredicate(Root<ClientProvider> root,
                    CriteriaQuery<?> query, CriteriaBuilder cb) {
                root.join(ClientProvider_.provider, JoinType.LEFT).join(
                        Provider_.specialty, JoinType.LEFT);
                if (clientId != null) {
                    return cb.equal(root.get(ClientProvider_.client), clientId);
                }
                return null;
            }
        };
    }
}
