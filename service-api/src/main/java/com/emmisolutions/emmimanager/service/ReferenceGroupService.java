package com.emmisolutions.emmimanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.model.ReferenceGroupType;

/**
 * Reference Group Service
 *
 */
public interface ReferenceGroupService {
	/**
	 * loads reference groups
	 * 
	 * @param Pageable
	 * @return Page<ReferenceGroup>
	 */
	Page<ReferenceGroup> loadReferenceGroups(Pageable page);

	/**
	 * find referenceGroup for given ReferenceGroupType
	 * 
	 * @param ReferenceGroupType
	 * @return ReferenceGroup
	 */
	ReferenceGroup findByType(ReferenceGroupType type);
}
