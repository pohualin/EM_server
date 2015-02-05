package com.emmisolutions.emmimanager.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.model.ReferenceTag;

/**
 * Reference Tag Persistence
 */
public interface ReferenceTagPersistence {

    /**
     * saves a given reference tag
     * @param tag to save
     * @return reference tag
     */
    ReferenceTag save(ReferenceTag tag);
        
    /**
     * finds all reference tags for a given reference group
     * @param group to find tags associated with
     * @return
     */
    Page<ReferenceTag> findAllByGroup(ReferenceGroup group, Pageable page);
    
    /**
     * reloads a given reference tag
     * @param tag to reload
     * @return
     */
    ReferenceTag reload(ReferenceTag tag);

}
