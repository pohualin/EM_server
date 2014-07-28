package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.Location;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Spring Data repo for Location objects
 */
public interface LocationRepository extends PagingAndSortingRepository<Location, Long>, JpaSpecificationExecutor<Location>{
}
