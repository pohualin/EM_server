package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.user.client.UserClientRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data repo for ClientLocation objects
 */
public interface UserClientRoleRepository extends JpaRepository<UserClientRole, Long>, JpaSpecificationExecutor<UserClientRole> {

    /**
     * Find all UserClientRole objects for a client
     *
     * @param clientId to find for
     * @param page     specification
     * @return a page of UserClientRole objects
     */
    Page<UserClientRole> findByClientId(long clientId, Pageable page);
}
