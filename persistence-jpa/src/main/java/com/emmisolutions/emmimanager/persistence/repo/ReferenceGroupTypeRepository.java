package com.emmisolutions.emmimanager.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.emmisolutions.emmimanager.model.ReferenceGroupType;

public interface ReferenceGroupTypeRepository extends JpaRepository<ReferenceGroupType, Long>, JpaSpecificationExecutor<ReferenceGroupType> {

}
