package com.emmisolutions.emmimanager.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.model.ReferenceGroupType;

/**
 * Reference Group Persistence
 */
public interface ReferenceGroupPersistence {

	/**
	 * loads reference groups
	 * @param Pageable
	 * @return Page<ReferenceGroup> 
	 */
	Page<ReferenceGroup> loadReferenceGroups(Pageable page);
	
	/**
	 * find ReferenceGroup by type
	 * @param String name
	 * @return ReferenceGroup
	 */
	ReferenceGroup findByType(ReferenceGroupType type);

}
