package com.emmisolutions.emmimanager.service;

import java.util.Collection;

import com.emmisolutions.emmimanager.model.Group;

/**
 * Group Service API
 *
 */
public interface GroupService {
	
	/**
	 * Eager fetch all reference groups
	 * @return HashSet of reference groups
	 */
	Collection<Group> fetchReferenceGroups();
}
