package com.emmisolutions.emmimanager.persistence.repo;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.emmisolutions.emmimanager.model.ReferenceGroup;

public interface ReferenceGroupRepository extends JpaRepository<ReferenceGroup, Long>, JpaSpecificationExecutor<ReferenceGroup>{
	
	@Query("select g from ReferenceGroup g")
	Set<ReferenceGroup> fetchReferenceGroups();
}
