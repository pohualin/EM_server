package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data repo for Location objects
 */
public interface LocationRepository extends JpaRepository<Location, Long>, JpaSpecificationExecutor<Location> {

    @Query("select l.id from Location l left join l.usingThisLocation u where u.id = :clientId")
    List<Long> findAllIdsByClientId(@Param("clientId")Long clientId);
}
