package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * TeamProviderTeamLocation repository
 */
public interface TeamProviderTeamLocationRepository extends JpaRepository<TeamProviderTeamLocation, Long>, JpaSpecificationExecutor<TeamProviderTeamLocation> {

    /**
     * finds a page of TeamProviderTeamLocations for a given TeamProvider
     *
     * @param teamProvider to use
     * @param pageable     specification
     * @return page of TPTL objects
     */
    Page<TeamProviderTeamLocation> findByTeamProvider(TeamProvider teamProvider, Pageable page);

    /**
     * Deletes all TeamProviderTeamLocations for a give teamProvider
     *
     * @param teamProvider to delete
     * @return the number deleted
     */
    long removeAllByTeamProvider(TeamProvider teamProvider);

    /**
     * Delete all TPTL objects for a Client and Provider
     *
     * @param client   to point to
     * @param location to use
     * @return number deleted
     */
    long deleteByTeamProviderTeamClientAndTeamLocationLocation(Client client, Location location);


    /**
     * Delete all TPTL objects for a Client and Provider
     *
     * @param client   to point to
     * @param provider to use
     * @return number deleted
     */
    long deleteByTeamProviderTeamClientAndTeamProviderProvider(Client client, Provider provider);
}
