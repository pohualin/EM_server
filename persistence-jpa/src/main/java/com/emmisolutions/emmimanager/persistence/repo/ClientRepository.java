package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.Client;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Client repo.
 */
public interface ClientRepository extends PagingAndSortingRepository<Client, Long>, JpaSpecificationExecutor<Client> {
}
