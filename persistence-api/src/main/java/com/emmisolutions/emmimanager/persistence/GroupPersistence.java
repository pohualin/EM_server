package com.emmisolutions.emmimanager.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.Group;
import com.emmisolutions.emmimanager.model.GroupSearchFilter;

/**
 * Group Persistence
 */
public interface GroupPersistence {

	/**
	 * Saves a group
	 * @param Group
	 * @return Group
	 */
	Group save(Group group);

	/**
	 * Lists groups by groupSearchFilter
	 * @param Pageable
	 * @param GroupSearchFilter
	 * @return Page<Group>
	 */
	Page<Group> list(Pageable page, GroupSearchFilter searchFilter);

	/**
	 * Reloads a group given the id
	 * @param Long group id
	 * @return Group
	 */
	Group reload(Long id);

	/**
	 * Delete all groups
	 * @param List<Group>
	 * @return void
	 */
	void removeAll(List<Group> groups);


}
