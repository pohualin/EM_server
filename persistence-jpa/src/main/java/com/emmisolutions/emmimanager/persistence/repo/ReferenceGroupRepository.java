package com.emmisolutions.emmimanager.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.model.ReferenceGroupType;
/**
 * Spring Data Repo for Reference Group
 */
public interface ReferenceGroupRepository extends JpaRepository<ReferenceGroup, Long>, JpaSpecificationExecutor<ReferenceGroup> {
	 /**
     * Find ReferenceGroup for given type
     * @param ReferenceGroupType type
     * @return ReferenceGroup
     */
	ReferenceGroup findByType(ReferenceGroupType type);

}
