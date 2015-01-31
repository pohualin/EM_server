package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data repo for UserClient Entities
 */
public interface UserClientRepository extends JpaRepository<UserClient, Long>,
        JpaSpecificationExecutor<UserClient> {

    /**
     * Find a user by the login
     *
     * @param login case insensitive comparison
     * @return UserClient or null
     */
    UserClient findByLoginIgnoreCase(String login);

    /**
     * Finds a UserClient by activation key
     *
     * @param activationKey the key
     * @return UserClient or null
     */
    UserClient findByActivationKey(String activationKey);
}
