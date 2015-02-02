package com.emmisolutions.emmimanager.service;

import java.util.Set;

import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.model.ReferenceTag;

/**
 * 
 * Reference Tag Service Interface
 *
 */
public interface ReferenceTagService {
    
    /**
     * Finds all tags for a given group
     * @param group - to find tags associated with
     * @return
     */
    Set<ReferenceTag> findAllTagsByGroup(ReferenceGroup group);
}
