package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.RefGroupSaveRequest;
import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.model.ReferenceGroupType;
import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.emmisolutions.emmimanager.persistence.GroupPersistence;
import com.emmisolutions.emmimanager.persistence.ReferenceGroupPersistence;
import com.emmisolutions.emmimanager.persistence.ReferenceGroupTypePersistence;
import com.emmisolutions.emmimanager.service.ReferenceGroupService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashSet;

/**
 * Reference Group Service Impl
 *
 */
@Service
public class ReferenceGroupServiceImpl implements ReferenceGroupService {

	@Resource
	ReferenceGroupPersistence referenceGroupPersistence;
	
	@Resource
	ReferenceGroupTypePersistence referenceGroupTypePersistence;

    @Resource
    GroupPersistence groupPersistence;

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
    public boolean isDeletable(ReferenceGroup group) {
        return !groupPersistence.doAnyGroupsUse(group);
    }

    @Override
    @Transactional
    public void delete(ReferenceGroup referenceGroup) {
        if (referenceGroup == null) {
            throw new InvalidDataAccessApiUsageException("Reference group needs to be persistent");
        }
        referenceGroupPersistence.delete(reload(referenceGroup.getId()));
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
        if (groupSaveRequest == null || groupSaveRequest.getReferenceGroup() == null ||
                CollectionUtils.isEmpty(groupSaveRequest.getReferenceTags())){
            throw new InvalidDataAccessApiUsageException("Tags cannot be null");
        }
        ReferenceGroup referenceGroup = groupSaveRequest.getReferenceGroup();

        ReferenceGroup savedReferenceGroup = referenceGroupPersistence.reload(referenceGroup.getId());
        if (savedReferenceGroup != null) {
            referenceGroup.setType(savedReferenceGroup.getType());
        }

        // create the type, if necessary
        String newTypeName = referenceGroup.getName().replaceAll(" ", "_").toUpperCase();
        if (referenceGroup.getType() == null) {
            // find if the type is already saved
            ReferenceGroupType savedType = referenceGroupTypePersistence.findByName(newTypeName);
            if (savedType != null) {
                referenceGroup.setType(savedType);
            } else {
                referenceGroup.setType(new ReferenceGroupType());
            }
        }
        referenceGroup.getType().setName(newTypeName);


        // add the tags to the group
        referenceGroup.setTags(new HashSet<ReferenceTag>());
        for (ReferenceTag referenceTag : groupSaveRequest.getReferenceTags()) {
            String trimmedName = StringUtils.trimToEmpty(referenceTag.getName());
            if (StringUtils.isNotBlank(trimmedName)) {
                referenceTag.setName(trimmedName);
                referenceTag.setGroup(referenceGroup);
                referenceGroup.getTags().add(referenceTag);
                if (referenceGroup.getId() == null) {
                    referenceGroup = referenceGroupPersistence.save(referenceGroup);
                }
            }
        }
        return referenceGroupPersistence.save(referenceGroup);
    }
}
