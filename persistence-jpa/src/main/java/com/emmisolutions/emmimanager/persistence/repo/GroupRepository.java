package com.emmisolutions.emmimanager.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.emmisolutions.emmimanager.model.Group;

/**
 * Spring Data Repo for Group Entities
 */
public interface GroupRepository extends JpaRepository<Group, Long>, JpaSpecificationExecutor<Group>{
}