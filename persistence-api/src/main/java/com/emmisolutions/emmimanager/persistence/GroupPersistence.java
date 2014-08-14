package com.emmisolutions.emmimanager.persistence;

import java.util.Collection;

import com.emmisolutions.emmimanager.model.Group;

public interface GroupPersistence {
	
	/**
     * Retrieves all reference groups
     *
     */
    Collection<Group> fetchReferenceGroups();

}
