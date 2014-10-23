package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data repo for ClientLocation objects
 */
public interface ClientLocationRepository extends JpaRepository<ClientLocation, Long>, JpaSpecificationExecutor<ClientLocation> {

    Page<ClientLocation> findByClient(Client client, Pageable page);
}
