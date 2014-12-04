package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.ClientLocation;
import com.emmisolutions.emmimanager.model.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Spring Data repo for ClientLocation objects
 */
public interface ClientLocationRepository extends JpaRepository<ClientLocation, Long>, JpaSpecificationExecutor<ClientLocation> {

    /**
     * Finds a page of locations by client id
     *
     * @param clientId to narrow by
     * @param page     the page specification
     * @return a page of matching ClientLocation objects
     */
    Page<ClientLocation> findByClientId(Long clientId, Pageable page);
    
    /**
     * Finds a page of clients by location id
     *
     * @param locationId to narrow by
     * @param page     the page specification
     * @return a page of matching ClientLocation objects
     */
    Page<ClientLocation> findByLocationId(Long locationId, Pageable page);

    /**
     * Finds a full list of ClientLocation objects for a single client for a
     * discrete list of locations
     *
     * @param clientId  to narrow by
     * @param locations the discrete list of locations we are interested in
     * @return a List of ClientLocation objects
     */
    List<ClientLocation> findByClientIdAndLocationIn(Long clientId, List<Location> locations);

    /**
     * Find a ClientLocation using its constitent parts
     *
     * @param clientId   to find
     * @param locationId to find
     * @return the ClientLocation or null
     */
    ClientLocation findByClientIdAndLocationId(Long clientId, Long locationId);
}
