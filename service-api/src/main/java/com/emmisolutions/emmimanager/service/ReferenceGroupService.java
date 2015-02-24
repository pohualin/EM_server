package com.emmisolutions.emmimanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.RefGroupSaveRequest;
import com.emmisolutions.emmimanager.model.ReferenceGroup;

/**
 * Reference Group Service
 *
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
     * updates a given reference group
     * @param group to update
     * @return
     */
    ReferenceGroup updateReferenceGroup(ReferenceGroup group);
    
    /**
     * reloads a ReferenceGroup for given id
     * @param id to load
     * @return
     */
    ReferenceGroup reload(Long id);
    
    /**
     * POST to create reference group and its tags
     * @param groupSaveRequests
     * @return
     */
    ReferenceGroup saveReferenceGroupAndReferenceTags(RefGroupSaveRequest groupSaveRequests);

    /**
     * saves a given reference group
     * @param group to save
     * @return
     */
    ReferenceGroup save(ReferenceGroup group);

}
