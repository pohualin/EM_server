package com.emmisolutions.emmimanager.service;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.Group;
import com.emmisolutions.emmimanager.model.GroupSearchFilter;
import com.emmisolutions.emmimanager.model.ReferenceGroup;

/**
 * Group Service API
 *
 */
public interface GroupService {

	/**
	 * Eager fetch all reference groups
	 * 
	 * @return HashSet of reference groups
	 */
	Collection<ReferenceGroup> fetchReferenceGroups();

	/**
	 * Saves a group
	 */
	Group save(Group group);

	/**
	 *	Returns a Page of Groups based on the search filter with default Page size
	 */
	Page<Group> list(GroupSearchFilter groupSearchFilter);

	/**
	 * Returns a Page of Groups based on the search filter
	 */
	Page<Group> list(Pageable pageable, GroupSearchFilter groupSearchFilter);

	/**
	 * Creates a group and associates client to it
	 */
	Group create(Group group, Long clientId);

	/**
	 * Updates the given group
	 */
	Group update(Group group);

	/**
	 * Find the group by given id
	 */
	Group reload(Long id);
	
	/**
	 * Delete the group by given id
	 */
	void remove(Long id);
}

