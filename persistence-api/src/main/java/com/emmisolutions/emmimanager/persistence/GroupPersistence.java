package com.emmisolutions.emmimanager.persistence;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.Group;
import com.emmisolutions.emmimanager.model.GroupSearchFilter;
import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.model.Tag;


public interface GroupPersistence {

	/**
	 * Retrieves all reference groups
	 *
	 */
	Collection<ReferenceGroup> fetchReferenceGroups();

	/**
	 * Saves a group
	 * @param Group
	 * @return Group
	 */
	Group save(Group group);

	/**
	 * List of groups associated with given Client ID
	 * @param Pageable
	 * @param GroupSearchFilter
	 */
	Page<Group> list(Pageable page, GroupSearchFilter searchFilter);

	/**
	 * Reloads a group given the id
	 * @param Long group id
	 * @return Group
	 */
	Group reload(Long id);
	
	/**
	 * 	Deletes a Group by given id
	 * @param Long	id
	 * @return void
	 */	
	void remove(Long id);
	
	List<Group> updateAll(List<Group> groups);
	
	List<Group> createAll(List<Group> groups);

	void removeAll(List<Group> groups);


}
