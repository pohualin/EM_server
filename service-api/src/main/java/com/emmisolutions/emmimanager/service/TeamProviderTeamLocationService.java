package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * TeamProviderTeamLocation Service API
 */
@Service
public interface TeamProviderTeamLocationService {
    /**
     * finds a page of TeamProviderTeamLocations for a given TeamProvider
     *
     * @param teamProvider the provider
     * @param pageable     specificaiton
     * @return page of TPTLs
     */
    Page<TeamProviderTeamLocation> findByTeamProvider(TeamProvider teamProvider, Pageable pageable);

    /**
     * finds a page of TeamProviderTeamLocations for a given TeamLocation
     *
     * @param teamLocation the location
     * @param pageable     specification
     * @return page of TPTLS
     */
    Page<TeamProviderTeamLocation> findByTeamLocation(TeamLocation teamLocation, Pageable pageable);

    /**
     * Saves a list if team provider team locations
     *
     * @param teamProviderTeamLocations list of teamProviderTeamLocations
     * @return list of teamProviderTeamLocations
     */
    Set<TeamProviderTeamLocation> saveAllTeamProviderTeamLocations(List<TeamProviderTeamLocation> teamProviderTeamLocations);

    /**
     * removes all TeamProviderTeamLocations for given teamProvider
     *
     * @param teamProvider to remove
     */
    void removeAllByTeamProvider(TeamProvider teamProvider);

    /**
     * update the TeamProviderTeamLocation for teamLocation
     *
     * @param teamLocation to update
     * @param request      with these values
     */
    void updateTeamProviderTeamLocations(TeamLocation teamLocation,
                                         TeamLocationTeamProviderSaveRequest request);

    /**
     * update the TeamProviderTeamLocation for teamProvider
     *
     * @param teamProvider to update
     * @param request      with team locations
     */
    void updateTeamProviderTeamLocations(TeamProvider teamProvider,
                                         TeamProviderTeamLocationSaveRequest request);

}
