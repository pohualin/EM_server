package com.emmisolutions.emmimanager.service.spring;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.emmisolutions.emmimanager.persistence.ReferenceTagPersistence;
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

    @Override
    @Transactional
    public Page<ReferenceTag> findAllTagsByGroup(ReferenceGroup group, Pageable page) {
        return referenceTagPersistence.findAllByGroup(group, page);
    }
}
