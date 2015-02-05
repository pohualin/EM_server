package com.emmisolutions.emmimanager.persistence.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.ReferenceGroupType;
import com.emmisolutions.emmimanager.persistence.ReferenceGroupTypePersistence;
import com.emmisolutions.emmimanager.persistence.repo.ReferenceGroupTypeRepository;

/**
 * ReferenceGroupPersistenceImpl implementation
 */
@Repository
public class ReferenceGroupTypePersistenceImpl implements ReferenceGroupTypePersistence {

    @Resource
    ReferenceGroupTypeRepository referenceGroupTypeRepository;

    @Override
    public ReferenceGroupType save(ReferenceGroupType groupType) {
        return referenceGroupTypeRepository.save(groupType);
    }
}
