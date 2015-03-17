package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * Group Persistence
 */
public interface GroupPersistence {

    /**
     * Saves a group
     *
     * @param group Group
     * @return Group
     */
    Group save(Group group);

    /**
     * Lists groups by groupSearchFilter
     *
     * @param page         specification
     * @param searchFilter GroupSearchFilter
     * @return Page<Group>
     */
    Page<Group> list(Pageable page, GroupSearchFilter searchFilter);

    /**
     * Reloads a group given the id
     *
     * @param id group id
     * @return Group
     */
    Group reload(Long id);

    /**
     * Remove tags at the client that are not in the set of group IDs
     *
     * @param clientId       to use
     * @param groupIdsToKeep eliminate tags not using these group ids
     * @return number deleted
     */
    long removeGroupsThatAreNotAssociatedWith(Long clientId, Set<Long> groupIdsToKeep);

    /**
     * Find the set of Teams that are already mapped to tags not present in the
     * save request.
     *
     * @param groupSaveRequests to check
     * @param clientId          for the scope of the tags to check
     * @return set of conflicting teams
     */
    Set<TeamTag> findTeamsPreventingSaveOf(List<GroupSaveRequest> groupSaveRequests, Long clientId);

    /**
     * Checks to see if any groups point to the saved reference group
     *
     * @param saved the reference group
     * @return true if groups are already using, false if groups are not
     */
    boolean doAnyGroupsUse(ReferenceGroup saved);
}
