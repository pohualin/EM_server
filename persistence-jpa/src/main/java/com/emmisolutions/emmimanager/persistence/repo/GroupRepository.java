package com.emmisolutions.emmimanager.persistence.repo;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.emmisolutions.emmimanager.model.Group;
import com.emmisolutions.emmimanager.model.User;

public interface GroupRepository extends JpaRepository<Group, Long>, JpaSpecificationExecutor<Group>{
	
	@Query("select g from ReferenceGroup g")
	Set<Group> fetchReferenceGroups();
}
