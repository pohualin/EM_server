package com.emmisolutions.emmimanager.service.spring;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emmisolutions.emmimanager.model.RefGroupSaveRequest;
import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.model.ReferenceGroupType;
import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.emmisolutions.emmimanager.persistence.ReferenceGroupPersistence;
import com.emmisolutions.emmimanager.persistence.ReferenceGroupTypePersistence;
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
	
	@Resource
	ReferenceGroupTypePersistence referenceGroupTypePersistence;
	
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
        ReferenceGroup savedGroup = referenceGroupPersistence.reload(groupSaveRequest.getReferenceGroup().getId());

        if (savedGroup == null) {
            validateGroupNameForNoDuplicate(groupSaveRequest);
            groupSaveRequest.getReferenceGroup().setType(getReferenceGroupType(groupSaveRequest.getReferenceGroup().getName()));
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
    
    private ReferenceGroupType getReferenceGroupType(String groupTypeName) {
        ReferenceGroupType groupType = new ReferenceGroupType();
        groupType.setName(groupTypeName.replace(" ", "_").toUpperCase());
        return referenceGroupTypePersistence.save(groupType);
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
    
    
    private void validateGroupNameForNoDuplicate(RefGroupSaveRequest saveRequest){
        ReferenceGroupType type = referenceGroupTypePersistence.findByName(saveRequest.getReferenceGroup().getName().replace(" ", "_").toUpperCase());
        if (type != null) {
            throw new IllegalArgumentException("Group name: '" + saveRequest.getReferenceGroup().getName() + "' is null, only contains special characters or is a duplicate");
        }  
    }
}
