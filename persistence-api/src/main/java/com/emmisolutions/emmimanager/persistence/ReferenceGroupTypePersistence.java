package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.ReferenceGroupType;

/**
 * Reference Group Type Persistence
 */
public interface ReferenceGroupTypePersistence {
	/**
	 * find ReferenceGroup by name
	 * @param String name
	 * @return ReferenceGroup
	 */
	ReferenceGroupType findByName(String name);

}
