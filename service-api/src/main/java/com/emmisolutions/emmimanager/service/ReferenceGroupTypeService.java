package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.ReferenceGroupType;
/**
 * Reference Group Type Service
 *
 */
public interface ReferenceGroupTypeService {
	/**
	 * Finds referenceGroupType by name
	 * @param String name
	 * @return ReferenceGroupType
	 */
	ReferenceGroupType findByName(String name);
}
