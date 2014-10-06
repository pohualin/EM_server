package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.persistence.GroupPersistence;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.GroupService;
import com.emmisolutions.emmimanager.service.TagService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of Group service
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
    @Transactional(readOnly = true)
    public Group reload(Long id) {
        if (id == null) {
            return null;
        }
        return groupPersistence.reload(id);
    }


    @Override
    @Transactional
    public Set<Group> saveGroupsAndTags(List<GroupSaveRequest> groupSaveRequests, Long clientId) {
        if (clientId == null) {
            throw new IllegalArgumentException("clientID cannot be null");
        }

        // remove all groups for a client
        groupPersistence.removeAll(clientId);

        Set<Group> groups = new HashSet<>();
        Client client = new Client();
        client.setId(clientId);
        client = clientService.reload(client);

        for (GroupSaveRequest request : groupSaveRequests) {
            request.getGroup().setClient(client);
            Group savedGroup = save(request.getGroup());
            List<Tag> savedTags = tagService.saveAllTagsForGroup(request.getTags(), savedGroup);
            if (savedTags == null || savedTags.isEmpty()) {
                throw new IllegalArgumentException("Tags cannot be null");
            }
            savedGroup.setTags(new HashSet<>(savedTags));
            groups.add(savedGroup);
        }
        return groups;
    }
}
