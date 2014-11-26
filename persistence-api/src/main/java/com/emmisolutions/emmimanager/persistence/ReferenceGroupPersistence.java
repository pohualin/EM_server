package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.ReferenceGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Reference Group Persistence
 */
public interface ReferenceGroupPersistence {

	/**
	 * loads reference groups
	 *
	 * @param page specification
	 * @return Page<ReferenceGroup>
	 */
	Page<ReferenceGroup> loadReferenceGroups(Pageable page);
}
