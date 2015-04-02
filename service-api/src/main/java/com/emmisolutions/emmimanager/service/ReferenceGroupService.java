package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.RefGroupSaveRequest;
import com.emmisolutions.emmimanager.model.ReferenceGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Reference Group Service
 */
public interface ReferenceGroupService {

    /**
     * loads reference groups
     *
     * @param Pageable
     * @return Page<ReferenceGroup>
     */
    Page<ReferenceGroup> loadReferenceGroups(Pageable page);
    
    /**
     * load active reference groups
     * 
     * @param page
     *            to use
     * @return a page of active reference groups
     */
    Page<ReferenceGroup> loadActiveReferenceGroups(Pageable page);

    /**
     * updates a given reference group
     *
     * @param group to update
     * @return
     */
    ReferenceGroup updateReferenceGroup(ReferenceGroup group);

    /**
     * reloads a ReferenceGroup for given id
     *
     * @param id to load
     * @return
     */
    ReferenceGroup reload(Long id);

    /**
     * POST to create reference group and its tags
     *
     * @param groupSaveRequests
     * @return
     */
    ReferenceGroup saveReferenceGroupAndReferenceTags(RefGroupSaveRequest groupSaveRequests);

    /**
     * saves a given reference group
     *
     * @param group to save
     * @return
     */
    ReferenceGroup save(ReferenceGroup group);


    /**
     * See if this group is deletable. A group is deletable if it is not
     * used by a client.
     *
     * @param group to check
     * @return true is deletable, false is not deletable
     */
    boolean isDeletable(ReferenceGroup group);

    /**
     * Delete a reference group
     *
     * @param referenceGroup do be removed
     */
    void delete(ReferenceGroup referenceGroup);
}
