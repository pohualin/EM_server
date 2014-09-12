package com.emmisolutions.emmimanager.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.emmisolutions.emmimanager.model.ReferenceGroup;

public interface ReferenceGroupRepository extends JpaRepository<ReferenceGroup, Long>, JpaSpecificationExecutor<ReferenceGroup> {

}
