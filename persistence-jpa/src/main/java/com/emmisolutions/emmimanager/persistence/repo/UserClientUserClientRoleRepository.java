package com.emmisolutions.emmimanager.persistence.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.UserClientUserClientRole;

/**
 * Spring Data repo for UserClient Entities
 */
public interface UserClientUserClientRoleRepository extends
        JpaRepository<UserClientUserClientRole, Long>,
        JpaSpecificationExecutor<UserClientUserClientRole> {

    /**
     * Find a page of UserClientUSerClientRole
     * 
     * @param userClient
     *            to lookup
     * @param pageable
     *            to use
     * @return a page of UserClientUSerClientRole
     */
    public Page<UserClientUserClientRole> findByUserClient(
            UserClient userClient, Pageable pageable);
}
