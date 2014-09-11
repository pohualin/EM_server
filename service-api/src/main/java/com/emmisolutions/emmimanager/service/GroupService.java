package com.emmisolutions.emmimanager.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.Group;
import com.emmisolutions.emmimanager.model.GroupSaveRequest;
import com.emmisolutions.emmimanager.model.GroupSearchFilter;

/**
 * Group Service API
 *
 */
public interface GroupService {

	/**
	 * Saves a group
	 */
	Group save(Group group);

	/**
	 * Returns a Page of Groups based on the search filter with default Page
	 * size
	 */
	Page<Group> list(GroupSearchFilter groupSearchFilter);

	/**
	 * Returns a Page of Groups based on the search filter
	 */
	Page<Group> list(Pageable pageable, GroupSearchFilter groupSearchFilter);

	/**
	 * Find the group by given id
	 */
	Group reload(Long id);

	/**
	 * Deletes all Groups passed in a List
	 */
	void removeAll(List<Group> groups);

	/**
	 * Saves a Group and it's tags
	 * @param List      <GroupSaveRequest>
	 * @param Long      clientID
	 * @return List<Group>
	 */
	public List<Group> saveGroupsAndTags(
			List<GroupSaveRequest> groupSaveRequests, Long clientId);

}