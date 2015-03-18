package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.ReferenceGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Reference Group Persistence
 */
public interface ReferenceGroupPersistence {

    /**
     * loads reference groups
     *
     * @param page specification
     * @return Page<ReferenceGroup>
     */
    Page<ReferenceGroup> loadReferenceGroups(Pageable page);

    /**
     * Saved a given ReferenceGroup
     *
     * @param group referenceGroup
     * @return ReferenceGroup
     */
    ReferenceGroup save(ReferenceGroup group);

    /**
     * Reloads a ReferenceGroup by given id
     *
     * @param id identifier for the group
     * @return ReferenceGroup
     */
    ReferenceGroup reload(Long id);

    /**
     * Delete a reference group
     *
     * @param referenceGroup to be deleted
     */
    void delete(ReferenceGroup referenceGroup);
}
