package com.emmisolutions.emmimanager.persistence.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.emmisolutions.emmimanager.model.Team;

/**
 * Spring data repo for Team objects
 */
public interface TeamRepository extends PagingAndSortingRepository<Team, Long>, JpaSpecificationExecutor<Team> {

}
