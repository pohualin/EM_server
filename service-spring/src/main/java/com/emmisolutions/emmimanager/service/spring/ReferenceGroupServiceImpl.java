package com.emmisolutions.emmimanager.service.spring;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emmisolutions.emmimanager.model.RefGroupSaveRequest;
import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.emmisolutions.emmimanager.persistence.ReferenceGroupPersistence;
import com.emmisolutions.emmimanager.persistence.ReferenceTagPersistence;
import com.emmisolutions.emmimanager.service.ReferenceGroupService;

/**
 * Reference Group Service Impl
 *
 */
@Service
public class ReferenceGroupServiceImpl implements ReferenceGroupService {

	@Resource
	ReferenceGroupPersistence referenceGroupPersistence;
	
	@Resource
	ReferenceTagPersistence referenceTagPersistence;
	
	@Override
	@Transactional(readOnly = true)
	public Page<ReferenceGroup> loadReferenceGroups(Pageable page) {
		return referenceGroupPersistence.loadReferenceGroups(page);
	}

	@Override
    @Transactional
    public ReferenceGroup save(ReferenceGroup group) {
        return referenceGroupPersistence.save(group);
    }
	
	@Override
	@Transactional
	public ReferenceGroup updateReferenceGroup(ReferenceGroup group) {
	    if (group == null || group.getId() == null){
	        throw new InvalidDataAccessApiUsageException("group cannot be null");
	    }
	    return referenceGroupPersistence.save(group);
	}
	
	@Override
	@Transactional
	public ReferenceGroup reload(Long id){
	    if (id == null){
            return null;
	    }
	    return referenceGroupPersistence.reload(id);
	}

    @Override
    @Transactional
    public ReferenceGroup saveReferenceGroupAndReferenceTags(RefGroupSaveRequest groupSaveRequest) {
        ensureGroupsAndTagsAreValid(groupSaveRequest);
        ReferenceGroup savedGroup = referenceGroupPersistence.reload(groupSaveRequest.getReferenceGroup().getId());
        if (savedGroup == null) {
            savedGroup = save(groupSaveRequest.getReferenceGroup());
        }

        if (groupSaveRequest.getReferenceTags() != null) {
            ReferenceTag reftag;
            savedGroup.getTags().clear();
            for (ReferenceTag t : groupSaveRequest.getReferenceTags()) {
                ReferenceTag fromDb = referenceTagPersistence.reload(t);
                if (fromDb == null) {
                    reftag = saveTagForGroup(t, savedGroup);
                } else {
                    reftag = fromDb;
                }
                savedGroup.getTags().add(reftag);
            }
        }

        if (savedGroup.getTags() == null || savedGroup.getTags().isEmpty()) {
            throw new InvalidDataAccessApiUsageException("Tags cannot be null");
        }

        return savedGroup;
    }
    
    private ReferenceTag saveTagForGroup(ReferenceTag tag, ReferenceGroup group) {
        ReferenceTag t = new ReferenceTag();
        if (tag != null) {
                String trimmedName = StringUtils.trimToEmpty(tag.getName());
                if (StringUtils.isNotBlank(trimmedName)) {
                    t.setId(tag.getId());
                    t.setName(trimmedName);
                    t.setGroup(group);
                }
        }
        return referenceTagPersistence.save(t);
    }
    
    private void ensureGroupsAndTagsAreValid(RefGroupSaveRequest saveRequest) {
        Set<String> groupNames = new HashSet<>();
        Set<String> tagsInAGroup = new HashSet<>();
        if (saveRequest.getReferenceGroup() != null) {
            String normalizedName = normalizeName(saveRequest.getReferenceGroup().getName());
            if (StringUtils.isBlank(normalizedName) || !groupNames.add(normalizedName)) {
                throw new IllegalArgumentException("Group name: '" + saveRequest.getReferenceGroup().getName() + "' is null, only contains special characters or is a duplicate");
            }
            if (saveRequest.getReferenceTags() != null) {
                for (ReferenceTag tag : saveRequest.getReferenceTags()) {
                    String normalizedTagName = normalizeName(tag.getName());
                    if (StringUtils.isBlank(normalizedTagName) || !tagsInAGroup.add(normalizedTagName)) {
                        throw new IllegalArgumentException("Tag name: '" + tag.getName() + "' is null, only contains special characters or is a duplicate within group: " + saveRequest.getReferenceGroup().getName());
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
}
