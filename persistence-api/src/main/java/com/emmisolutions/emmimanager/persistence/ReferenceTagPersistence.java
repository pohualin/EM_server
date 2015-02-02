package com.emmisolutions.emmimanager.persistence;

import java.util.Set;

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
    Set<ReferenceTag> findAllByGroup(ReferenceGroup group);
}
