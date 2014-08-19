package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.LocationSearchFilter;
import com.emmisolutions.emmimanager.model.Location_;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.LocationPersistence;
import com.emmisolutions.emmimanager.persistence.repo.LocationRepository;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import static com.emmisolutions.emmimanager.persistence.impl.specification.LocationSpecifications.*;
import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * Location Persistence API implementation
 */
@Repository
public class LocationPersistenceImpl implements LocationPersistence {

    @Resource
    LocationRepository locationRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Resource
    ClientPersistence clientPersistence;

    @Override
    public Page<Location> list(Pageable page, LocationSearchFilter filter) {
        if (page == null) {
            // default pagination request if none
            page = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        }
        Client client = null;
        if (filter != null && filter.getClientId() != null){
            client = clientPersistence.reload(filter.getClientId());
        }
        Page<Location> ret = locationRepository.findAll(where(usedBy(client)).and(hasNames(filter)).and(isInStatus(filter)), page);

        if (ret.hasContent() && client == null) {
            // eagerly load all clients using the locations on this page, l1 cache should hook everything up
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            EntityGraph<Location> graph = entityManager.createEntityGraph(Location.class);
            graph.addAttributeNodes(Location_.usingThisLocation);
            CriteriaQuery<Location> cq = cb.createQuery(Location.class);
            Root<Location> root = cq.from(Location.class);
            cq.select(root).where(root.in(ret.getContent()));
            entityManager.createQuery(cq)
                    .setHint(QueryHints.LOADGRAPH, graph)
                    .getResultList();
        }
        return ret;
    }

    @Override
    public Location save(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public Location reload(Location location) {
        if (location == null || location.getId() == null) {
            return null;
        }
        return locationRepository.findOne(location.getId());
    }
}
