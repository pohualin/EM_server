package com.emmisolutions.emmimanager.service.spring;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<ReferenceTag> findAllTagsByGroup(ReferenceGroup group, Pageable page) {
        ReferenceGroup groupFromDb = referenceGroupService.reload(group.getId());
        if (groupFromDb == null || groupFromDb.getId() == null) {
            throw new InvalidDataAccessApiUsageException("Reference Group cannot be null");
        }
        return referenceTagPersistence.findAllByGroup(group, page);
    }
}
