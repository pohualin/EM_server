package com.emmisolutions.emmimanager.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.ReferenceGroupType;

/**
 * Reference Group Type Persistence
 */
public interface ReferenceGroupTypePersistence {

    /**
     * Saved the reference group type
     * @param groupType to save
     * @return
     */
    ReferenceGroupType save(ReferenceGroupType groupType);
    
    /**
     * finds all reference group types
     * @param page specification
     * @return
     */
    Page<ReferenceGroupType> findAll(Pageable page);

    /**
     * finds a reference group type by given name
     * @param groupTypeName to find by
     * @return
     */
    ReferenceGroupType findByName(String groupTypeName);

}
