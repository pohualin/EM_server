package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.Group;
import com.emmisolutions.emmimanager.model.TeamTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Spring Data Repo for Group Entities
 */
public interface GroupRepository extends JpaRepository<Group, Long>, JpaSpecificationExecutor<Group> {

    /**
     * Remove all Groups by the client id
     *
     * @param forThisClientId           used to locate groups
     * @param notInTheseGroupIds where the group is not in this set
     * @return number of groups deleted
     */
    long removeByClientIdEqualsAndIdNotIn(Long forThisClientId, Set<Long> notInTheseGroupIds);

    /**
     * Remove all Groups by the client id
     *
     * @param forThisClientId used to locate groups
     * @return number of groups deleted
     */
    long removeByClientIdEquals(Long forThisClientId);

    /**
     * Find the set of Teams that are already mapped to tags not present in the
     * save request.
     * @param notInTheseTags to check
     * @param clientId for the scope of the tags to check
     * @return list of conflicting teams
     */
    @Query("select tt from TeamTag tt where tt.team.client.id = :clientId and tt.tag.id not in (:notInTheseTags)")
    List<TeamTag> findTeamsPreventingSaveOf(@Param("clientId") Long clientId, @Param("notInTheseTags") Collection<Long> notInTheseTags);
}
