package com.emmisolutions.emmimanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    Page<ReferenceTag> findAllTagsByGroup(ReferenceGroup group, Pageable page);
}
