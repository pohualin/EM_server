package com.emmisolutions.emmimanager.persistence.impl;

import java.util.Collection;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.Group;
import com.emmisolutions.emmimanager.persistence.GroupPersistence;
import com.emmisolutions.emmimanager.persistence.repo.GroupRepository;

@Repository
public class GroupPersistenceImpl implements GroupPersistence{
	
	@Resource
	GroupRepository groupRepository;
	
	@Override
	public Collection<Group> fetchReferenceGroups(){
		return groupRepository.fetchReferenceGroups();
	}
}
