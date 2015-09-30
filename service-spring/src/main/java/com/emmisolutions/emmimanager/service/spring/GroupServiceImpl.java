package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.persistence.GroupPersistence;
import com.emmisolutions.emmimanager.persistence.TagPersistence;
import com.emmisolutions.emmimanager.persistence.TeamTagPersistence;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.GroupService;
import com.emmisolutions.emmimanager.service.TagService;
import org.apache.commons.lang3.StringUtils;
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
    TagPersistence tagPersistence;

    @Resource
    TeamTagPersistence teamTagPersistence;

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
        return groupPersistence.reload(id);
    }


    @Override
    @Transactional
    public Set<Group> saveGroupsAndTags(List<GroupSaveRequest> groupSaveRequests, Long clientId) {
        Set<Group> groups = new HashSet<>();
        Client client = new Client();
        client.setId(clientId);
        client = clientService.reload(client);
        if (client != null) {
            // make sure groups (and tags) are not duplicates
            ensureGroupsAndTagsAreValid(groupSaveRequests);

            // delete tags and groups that will be orphaned by the save request
            cleanOutOrphans(groupSaveRequests, clientId);

            // add the groups back
            for (GroupSaveRequest request : groupSaveRequests) {
                if (request.getGroup() != null) {
                    // save the group
                    request.getGroup().setClient(client);
                    Group savedGroup = save(request.getGroup());

                    // save the tags pointing to the group
                    List<Tag> savedTags = tagService.saveAllTagsForGroup(request.getTags(), savedGroup);
                    if (savedTags == null || savedTags.isEmpty()) {
                        throw new IllegalArgumentException("Tags cannot be null");
                    }
                    // update group tag collection for automatic orphan management
                    savedGroup.getTags().clear();
                    savedGroup.getTags().addAll(savedTags);
                    groups.add(savedGroup);
                }
            }
        }
        return groups;
    }

    @Override
    public Set<TeamTag> findTeamsPreventingSaveOf(List<GroupSaveRequest> groupSaveRequests, Long clientId) {
        return groupPersistence.findTeamsPreventingSaveOf(groupSaveRequests, clientId);
    }

    private void cleanOutOrphans(List<GroupSaveRequest> groupSaveRequests, Long clientId) {
        Set<Long> groupIdsToKeep = new HashSet<>();
        for (GroupSaveRequest groupSaveRequest : groupSaveRequests) {
            if (groupSaveRequest.getGroup() != null && groupSaveRequest.getGroup().getId() != null) {
                groupIdsToKeep.add(groupSaveRequest.getGroup().getId());
            }
        }
        // clear out the orphan team tags, in groups we are not keeping
        teamTagPersistence.removeTeamTagsThatAreNotAssociatedWith(clientId, groupIdsToKeep);

        // clear out the orphan tags in the groups we aren't keeping
        tagPersistence.removeTagsThatAreNotAssociatedWith(clientId, groupIdsToKeep);

        // clear out the orphan groups
        groupPersistence.removeGroupsThatAreNotAssociatedWith(clientId, groupIdsToKeep);
    }

    private void ensureGroupsAndTagsAreValid(List<GroupSaveRequest> saveRequests) {
        Set<String> groupNames = new HashSet<>();
        for (GroupSaveRequest saveRequest : saveRequests) {
            Set<String> tagsInAGroup = new HashSet<>();
            if (saveRequest.getGroup() != null) {
                String normalizedName = normalizeName(saveRequest.getGroup().getName());
                if (StringUtils.isBlank(normalizedName) || !groupNames.add(normalizedName)) {
                    throw new IllegalArgumentException("Group name: '" + saveRequest.getGroup().getName() + "' is null, only contains special characters or is a duplicate");
                }
                if (saveRequest.getTags() != null) {
                    for (Tag tag : saveRequest.getTags()) {
                        String normalizedTagName = normalizeTagName(tag.getName());
                        if (StringUtils.isBlank(normalizedTagName) || !tagsInAGroup.add(normalizedTagName)) {
                            throw new IllegalArgumentException("Tag name: '" + tag.getName() + "' is null or is a duplicate within group: " + saveRequest.getGroup().getName());
                        }
                    }
                }
            }
        }
    }

    private String normalizeName(String name) {
        String normalizedName = StringUtils.trimToEmpty(StringUtils.lowerCase(name));
        if (StringUtils.isNotBlank(normalizedName)) {
            // do regex
            normalizedName = normalizedName.replaceAll("[^a-z0-9]*", "");
        }
        return normalizedName;
    }

    private String normalizeTagName(String name) {
        String trimmed = StringUtils.trimToEmpty(StringUtils.lowerCase(name));
        String normalizedName = null;
        if (StringUtils.isNotBlank(trimmed)) {
            // do regex
            normalizedName = trimmed.replaceAll("[^a-z0-9]*", "");
            if (!StringUtils.isNotBlank(normalizedName)){
                normalizedName = trimmed;
            }
        }
        return normalizedName;
    }

}
