package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * TeamProviderTeamLocation persistence class
 */
public interface TeamProviderTeamLocationPersistence {

    /**
     * Saves a list if team provider team locations
     *
     * @param teamProviderTeamLocations list of teamProviderTeamLocations
     * @return list of teamProviderTeamLocations
     */
    List<TeamProviderTeamLocation> saveAll(List<TeamProviderTeamLocation> teamProviderTeamLocations);
    
    
    /**
     * delete a list if team provider team locations
     *
     * @param list of teamProviderTeamLocations to be deleted
     */
    void delete(List<TeamProviderTeamLocation> teamProviderTeamLocations);

    /**
     * finds a page of TeamProviderTeamLocations for a given TeamProvider
     *
     * @param teamProvider to find by
     * @param pageable     page specification
     * @return page of team provider team location objects
     */
    Page<TeamProviderTeamLocation> findByTeamProvider(TeamProvider teamProvider, Pageable pageable);

    /**
     * removes all TeamProviderTeamLocations for given teamProvider
     *
     * @param teamProvider to remove
     */
    void removeAllByTeamProvider(TeamProvider teamProvider);

    /**
     * Remove all TPTLs for a Client and Location
     *
     * @param client   to use
     * @param location to use
     */
    void removeAllByClientLocation(Client client, Location location);


    /**
     * Remove all TPTLs for a Client and Provider
     *
     * @param client   to use
     * @param provider to use
     */
    void removeAllByClientProvider(Client client, Provider provider);
}
