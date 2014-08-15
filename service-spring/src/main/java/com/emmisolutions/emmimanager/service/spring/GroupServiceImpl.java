package com.emmisolutions.emmimanager.service.spring;

import java.util.Collection;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emmisolutions.emmimanager.model.Group;
import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.persistence.GroupPersistence;
import com.emmisolutions.emmimanager.service.GroupService;

/**
 * 
 * Implementation of Group service
 *
 */
@Service
public class GroupServiceImpl implements GroupService {
	
	@Resource
    GroupPersistence groupPersistence;

	@Override
	public Collection<ReferenceGroup> fetchReferenceGroups() {
		return groupPersistence.fetchReferenceGroups();
	}

    @Override
    @Transactional
    public Group save(Group group) {
    	return groupPersistence.save(group);
    }
}
