package com.emmisolutions.emmimanager.persistence.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamLocation;

/**
 * TeamLocation repo.
 */
public interface TeamLocationRepository extends JpaRepository<TeamLocation, Long>, JpaSpecificationExecutor<TeamLocation> {
    /*
    * Find all of the teamLocations that have a given team
    * @param team to search for
    * @return a Page of TeamLocations or null
    */
    Page<TeamLocation> findByTeam(Team team, Pageable pageable);

    /**
     * Delete all TeamLocations with given team
     * @param team to delete
     */
    void deleteByTeam(Team team);
}