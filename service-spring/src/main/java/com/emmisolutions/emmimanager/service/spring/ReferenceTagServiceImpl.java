package com.emmisolutions.emmimanager.service.spring;

import java.util.Set;

import javax.annotation.Resource;
import javax.transaction.Transactional;

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
}
