package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.Group;
import com.emmisolutions.emmimanager.model.GroupSaveRequest;
import com.emmisolutions.emmimanager.model.GroupSearchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * Group Service API
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
     * Saves a list of group saves requests for a client. This method
     * overwrites/merges such that the groups (and tags) on the client will
     * look exactly as the list coming in on the save request.
     *
     * @param groupSaveRequests the List of new groups and tags for this client
     * @param clientId          the client on which to set the groups
     * @return the set of saved groups
     */
    public Set<Group> saveGroupsAndTags(
            List<GroupSaveRequest> groupSaveRequests, Long clientId);

}