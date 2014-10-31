package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

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
}
