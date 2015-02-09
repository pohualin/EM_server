package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.ReferenceGroupType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Reference Group Type Persistence
 */
public interface ReferenceGroupTypePersistence {

    /**
     * Saved the reference group type
     *
     * @param groupType to save
     * @return the persistent type
     */
    ReferenceGroupType save(ReferenceGroupType groupType);

    /**
     * finds all reference group types
     *
     * @param page specification
     * @return a page of types
     */
    Page<ReferenceGroupType> findAll(Pageable page);

    /**
     * finds a reference group type by given name
     *
     * @param groupTypeName to find by
     * @return the type
     */
    ReferenceGroupType findByName(String groupTypeName);

}
