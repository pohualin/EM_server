package com.emmisolutions.emmimanager.persistence;

import java.util.Collection;

import com.emmisolutions.emmimanager.model.Group;
import com.emmisolutions.emmimanager.model.ReferenceGroup;

public interface GroupPersistence {
	
	/**
     * Retrieves all reference groups
     *
     */
    Collection<ReferenceGroup> fetchReferenceGroups();

    Group save(Group group);
}
