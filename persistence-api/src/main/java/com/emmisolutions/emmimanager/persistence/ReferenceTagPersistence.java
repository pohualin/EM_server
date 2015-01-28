package com.emmisolutions.emmimanager.persistence;

import java.util.List;
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
     * reloads a reference tag for given id
     * @param id to reload
     * @return reference tag
     */
    ReferenceTag reload(Long id);
    
    /**
     * creates tags from the given list
     * @param createTagsList list of tags to create
     * @return list of created tags
     */
    List<ReferenceTag> createAll(List<ReferenceTag> createTagsList);
    
    /**
     * deletes a given reference tag 
     * @param tag to delete
     */
    void delete(ReferenceTag tag);
    
    /**
     * finds all reference tags for a given reference group
     * @param group to find tags associated with
     * @return
     */
    Set<ReferenceTag> findAllByGroup(ReferenceGroup group);
}
