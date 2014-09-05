package com.emmisolutions.emmimanager.service.spring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Group;
import com.emmisolutions.emmimanager.model.GroupSaveRequest;
import com.emmisolutions.emmimanager.model.GroupSearchFilter;
import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.persistence.GroupPersistence;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.GroupService;
import com.emmisolutions.emmimanager.service.TagService;

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
	TagService tagService;
	
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
	public Group reload(Long id) {
		if (id == null) {
			return null;
		}
		return groupPersistence.reload(id);
	}

	@Override
	@Transactional
	public void removeAll(List<Group> groups) {
		groupPersistence.removeAll(groups);
	}
	
	@Override
	@Transactional
	public List<Group> saveGroupsAndTags(List<GroupSaveRequest> groupSaveRequests, Long clientId) {
		List<Group> groups = new ArrayList<Group>();

		Client client = new Client();
		client.setId(clientId);
		client = clientService.reload(client);
		
		for (GroupSaveRequest request : groupSaveRequests) {
			
			request.getGroup().setClient(client);
			Group savedGroup = save(request.getGroup());
			if (request.getTags() != null && !request.getTags().isEmpty()) {
				List<Tag> savedTags = tagService.saveAllTagsForGroup(request.getTags(), savedGroup);
				savedGroup.setTags(new HashSet<Tag>(savedTags));
			}
			groups.add(savedGroup);
		}
		return groups;
	}
}
