package com.emmisolutions.emmimanager.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.emmisolutions.emmimanager.model.user.client.UserClient;

/**
 * Spring Data repo for UserClient Entities
 */
public interface UserClientRepository extends JpaRepository<UserClient, Long>,
	JpaSpecificationExecutor<UserClient> {
    
}
