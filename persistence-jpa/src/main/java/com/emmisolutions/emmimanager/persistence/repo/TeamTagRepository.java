package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * TeamTag repo.
 */
public interface TeamTagRepository extends JpaRepository<TeamTag, Long>, JpaSpecificationExecutor<TeamTag> {
    /*
    * Find all of the teamTags that have a given team
    * @param team to search for
    * @return a Page of TeamTags or null
    */
    Page<TeamTag> findByTeam(Team team, Pageable pageable);

    /**
     * Delete all TeamTags with given team
     * @param team to delete
     */
    void deleteByTeam(Team team);
}
