package com.emmisolutions.emmimanager.persistence;

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

}
