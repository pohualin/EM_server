package com.emmisolutions.emmimanager.service;

import java.util.Collection;
import java.util.List;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Group;
import com.emmisolutions.emmimanager.model.ReferenceGroup;

/**
 * Group Service API
 *
 */
public interface GroupService {
	
	/**
	 * Eager fetch all reference groups
	 * @return HashSet of reference groups
	 */
	Collection<ReferenceGroup> fetchReferenceGroups();

	Group save(Group group);

}