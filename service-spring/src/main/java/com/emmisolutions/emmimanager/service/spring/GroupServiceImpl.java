package com.emmisolutions.emmimanager.service.spring;

import java.util.Collection;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Group;
import com.emmisolutions.emmimanager.model.GroupSearchFilter;
import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.persistence.GroupPersistence;
import com.emmisolutions.emmimanager.service.ClientService;
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

	@Resource
	ClientService clientService;

	@Override
	public Collection<ReferenceGroup> fetchReferenceGroups() {
		return groupPersistence.fetchReferenceGroups();
	}

	@Override
	@Transactional
	public Group save(Group group) {
		return groupPersistence.save(group);
	}

	@Override
	@Transactional
	public Group create(Group group, Long clientId) {
		if (group == null) {
			throw new IllegalArgumentException("group cannot be null");
		}

		Client toFind = new Client();
		toFind.setId(clientId);
		toFind = clientService.reload(toFind);
		group.setClient(toFind);

		return save(group);
	}

    @Override
    public Page<Group> list(GroupSearchFilter searchFilter) {
        return groupPersistence.list(null, searchFilter);
    }
    
	@Override
	@Transactional(readOnly = true)
	public Page<Group> list(Pageable pageable, GroupSearchFilter searchFilter) {
		return groupPersistence.list(pageable, searchFilter);
	}

	@Override
	@Transactional
	public Group update(Group group) {
		if (group == null || group.getId() == null) {
			throw new IllegalArgumentException("Group cannot be null.");
		}
		return groupPersistence.save(group);
	}

	@Override
	@Transactional
	public Group reload(Long id) {
		if (id == null) {
			return null;
		}
		return groupPersistence.reload(id);
	}
}
