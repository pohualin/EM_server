package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

/**
 * TeamTag repo.
 */
public interface TeamTagRepository extends JpaRepository<TeamTag, Long>, JpaSpecificationExecutor<TeamTag> {
    /**
     * Find all of the teamTags that have a given team
     *
     * @param team     to search for
     * @param pageable the page spec
     * @return a Page of TeamTags or null
     */
    Page<TeamTag> findByTeam(Team team, Pageable pageable);

    /**
     * Delete all TeamTags with given team
     *
     * @param team to delete
     */
    void deleteByTeam(Team team);

    /**
     * Remove all TeamTags for a client id where the group ids are not in the Set
     *
     * @param forThisClientId    to use
     * @param notInTheseGroupIds not these group ids
     * @return number of TeamTags removed
     */
    long deleteByTeamClientIdEqualsAndTagGroupIdNotIn(Long forThisClientId, Set<Long> notInTheseGroupIds);

    /**
     * Remove all TeamTags for a client id
     *
     * @param forThisClientId to use
     * @return number of TeamTags removed
     */
    long deleteByTeamClientIdEquals(Long forThisClientId);

    /**
     * Find the teamTags on a client and in a group
     * @param tagId for the scope of the tags to check
     * @return list of matching teamTags
     */
    @Query("select tt from TeamTag tt where tt.tag.id = :tagId ")
    Page<TeamTag> findTeamsWithTagId(@Param("tagId") Long tagId, Pageable pageable);
}
