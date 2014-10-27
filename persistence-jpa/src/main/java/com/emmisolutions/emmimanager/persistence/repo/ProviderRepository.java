package com.emmisolutions.emmimanager.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.emmisolutions.emmimanager.model.Provider;

/**
 * Spring Data Repo for Provider
 */
public interface ProviderRepository extends JpaRepository<Provider, Long>, JpaSpecificationExecutor<Provider> {

}
