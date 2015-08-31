package com.emmisolutions.emmimanager.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.emmisolutions.emmimanager.model.schedule.Encounter;

/**
 * Spring Data Repo for Encounter objects
 */
public interface EncounterRepository extends JpaRepository<Encounter, Long>,
        JpaSpecificationExecutor<Encounter> {

}
