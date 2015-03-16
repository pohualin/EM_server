package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data repo for ClientLocation objects
 */
public interface UserClientTeamRoleRepository extends JpaRepository<UserClientTeamRole, Long>, JpaSpecificationExecutor<UserClientTeamRole> {

    /**
     * Find all UserClientTeamRole objects for a client
     *
     * @param clientId to find for
     * @param page     specification
     * @return a page of UserClientTeamRole objects
     */
    Page<UserClientTeamRole> findByClientId(long clientId, Pageable page);


}
