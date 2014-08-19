package com.emmisolutions.emmimanager.persistence;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.Group;
import com.emmisolutions.emmimanager.model.GroupSearchFilter;
import com.emmisolutions.emmimanager.model.ReferenceGroup;

public interface GroupPersistence {

	/**
	 * Retrieves all reference groups
	 *
	 */
	Collection<ReferenceGroup> fetchReferenceGroups();

	/**
	 * Saves a group
	 */
	Group save(Group group);

	/**
	 * List of groups associated with given Client ID
	 */
	Page<Group> list(Pageable page, GroupSearchFilter searchFilter);

	/**
	 * Reloads a group given the id
	 */
	Group reload(Long id);
}
