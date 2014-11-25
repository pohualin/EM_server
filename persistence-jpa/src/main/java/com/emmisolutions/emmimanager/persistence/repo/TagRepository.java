package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.model.TeamTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * Spring Data Repo for Tag Entities
 */
public interface TagRepository extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {

    /**
     * Remove all tags for a client id where the group ids are not in the Set
     *
     * @param forThisClientId    to use
     * @param notInTheseGroupIds not these group ids
     * @return number deleted
     */
    long deleteByGroupClientIdEqualsAndGroupIdNotIn(Long forThisClientId, Set<Long> notInTheseGroupIds);

    /**
     * Remove all tags for a client id
     *
     * @param forThisClientId to use
     * @return number deleted
     */
    long deleteByGroupClientIdEquals(Long forThisClientId);

    /**
     * Find the teamTags on a client and in a group
     * @param tagId for the scope of the tags to check
     * @return list of matching teamTags
     */
    @Query("select tt from TeamTag tt where tt.tag.id = :tagId ")
    List<TeamTag> findTeamsWithTagId(@Param("tagId") Long tagId);
}
