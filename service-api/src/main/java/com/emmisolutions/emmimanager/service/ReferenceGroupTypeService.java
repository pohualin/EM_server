package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.ReferenceGroupType;


/**
 * Reference Group Service
 *
 */
public interface ReferenceGroupTypeService {
	/**
	 * loads reference groups
	 * @param Pageable
	 * @return Page<ReferenceGroup> 
	 */
//	Page<ReferenceGroup> loadReferenceGroups(Pageable page);

	ReferenceGroupType findByName(String name);
}
