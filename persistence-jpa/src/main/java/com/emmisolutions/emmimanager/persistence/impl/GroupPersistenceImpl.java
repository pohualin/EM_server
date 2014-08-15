package com.emmisolutions.emmimanager.persistence.impl;

import java.util.Collection;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.Group;
import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.persistence.GroupPersistence;
import com.emmisolutions.emmimanager.persistence.repo.GroupRepository;
import com.emmisolutions.emmimanager.persistence.repo.ReferenceGroupRepository;

@Repository
public class GroupPersistenceImpl implements GroupPersistence{
	
	@Resource
	GroupRepository groupRepository;
	
	@Resource
	ReferenceGroupRepository referenceGroupRepository;
	
	@Override
	public Collection<ReferenceGroup> fetchReferenceGroups(){
		return referenceGroupRepository.fetchReferenceGroups();
	}
	@Override
	
	public Group save(Group group){
		return groupRepository.save(group);
	}
	
	
}
