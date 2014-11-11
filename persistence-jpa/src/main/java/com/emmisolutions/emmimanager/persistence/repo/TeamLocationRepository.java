package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * TeamLocation repo.
 */
public interface TeamLocationRepository extends JpaRepository<TeamLocation, Long>, JpaSpecificationExecutor<TeamLocation> {
    /**
     * Find all of the teamLocations that have a given team
     *
     * @param team     to search for
     * @param pageable the page specification
     * @return a Page of TeamLocations or null
     */
    Page<TeamLocation> findByTeam(Team team, Pageable pageable);

    /**
     * Delete all TeamLocation objects for a Client and Location
     *
     * @param client   to point to
     * @param location to use
     * @return number deleted
     */
    long deleteByTeamClientAndLocation(Client client, Location location);

    /**
     * Finds a page of TeamLocations for a client and location
     *
     * @param client   the client
     * @param location the location
     * @param page     specification
     * @return page of Teams
     */
    @Query("select tl.team from TeamLocation tl where tl.team.client = :client and tl.location = :location")
    Page<Team> findTeamsByClientAndLocation(@Param("client") Client client, @Param("location") Location location, Pageable page);
}
