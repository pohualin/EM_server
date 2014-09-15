package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Client repo.
 */
public interface ClientRepository extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {
    /**
     * Find a client by the normalizedName.
     * The normalizedName is always changed to lower case, because the column normalizedName value is always stored as lower case 
     *
     * @param normalizedName case sensitive comparison
     * 
     * @return Client or null
     */
    Client findByNormalizedName(String normalizedName);
}
