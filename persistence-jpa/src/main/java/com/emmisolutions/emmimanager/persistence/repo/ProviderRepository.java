package com.emmisolutions.emmimanager.persistence.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.Team;

/**
 * Spring Data Repo for Provider
 */
public interface ProviderRepository extends JpaRepository<Provider, Long>, JpaSpecificationExecutor<Provider> {
	 /**
     * Find providers for given team
     * @param Pageable 
     * @param Team team for which providers to load
     * @return Page<Provider> provider page
     */
	Page<Provider> findByTeams(Pageable pageable, Team team);
}
