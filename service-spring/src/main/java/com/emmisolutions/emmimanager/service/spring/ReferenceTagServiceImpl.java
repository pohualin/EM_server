package com.emmisolutions.emmimanager.service.spring;

import java.util.Set;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.emmisolutions.emmimanager.persistence.ReferenceTagPersistence;
import com.emmisolutions.emmimanager.service.ReferenceGroupService;
import com.emmisolutions.emmimanager.service.ReferenceTagService;

/**
 * 
 * Reference Tag Service Impl
 *
 */

@Service
public class ReferenceTagServiceImpl implements ReferenceTagService {

    @Resource
    ReferenceTagPersistence referenceTagPersistence;

    @Resource
    ReferenceGroupService referenceGroupService;

    @Override
    @Transactional
    public Set<ReferenceTag> findAllTagsByGroup(ReferenceGroup group) {
        return referenceTagPersistence.findAllByGroup(group);
    }

    @Override
    @Transactional
    public ReferenceTag create(ReferenceTag tag) {
        if (tag == null || tag.getGroup() == null || tag.getGroup().getId() == null) {
            throw new InvalidDataAccessApiUsageException("tag or group cannot be null");
        }
        ReferenceGroup groupFromDb = referenceGroupService.reload(tag.getGroup().getId());
        tag.setGroup(groupFromDb);
        return referenceTagPersistence.save(tag);
    }

    @Override
    @Transactional
    public void deleteReferenceTag(ReferenceTag tag) {
        ReferenceTag tagToDelete = referenceTagPersistence.reload(tag.getId());
        referenceTagPersistence.delete(tagToDelete);
    }
}
