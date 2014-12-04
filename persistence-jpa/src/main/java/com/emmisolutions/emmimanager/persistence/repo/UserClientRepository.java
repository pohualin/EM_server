package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data repo for UserClient Entities
 */
public interface UserClientRepository extends JpaRepository<UserClient, Long>, JpaSpecificationExecutor<UserClient> {


}
