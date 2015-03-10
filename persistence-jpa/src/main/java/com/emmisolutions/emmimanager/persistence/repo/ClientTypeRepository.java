package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.ClientType;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Client repo.
 */
public interface ClientTypeRepository extends JpaRepository<ClientType, Long>, JpaSpecificationExecutor<ClientType> {

    @Override
    @Cacheable(value = "allClientTypes")
    List<ClientType> findAll();
}
