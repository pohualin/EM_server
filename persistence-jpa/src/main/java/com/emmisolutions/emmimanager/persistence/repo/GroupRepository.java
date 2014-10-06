package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data Repo for Group Entities
 */
public interface GroupRepository extends JpaRepository<Group, Long>, JpaSpecificationExecutor<Group> {

    /**
     * Remove all Groups by the client id
     *
     * @param clientId used to locate groups
     * @return number of groups deleted
     */
    Long removeByClientIdEquals(Long clientId);
}
