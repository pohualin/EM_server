package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.ReferenceGroupType;
import com.emmisolutions.emmimanager.persistence.ReferenceGroupTypePersistence;
import com.emmisolutions.emmimanager.persistence.repo.ReferenceGroupTypeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * ReferenceGroupTypePersistenceImpl implementation
 */
@Repository
public class ReferenceGroupTypePersistenceImpl implements ReferenceGroupTypePersistence {

    @Resource
    ReferenceGroupTypeRepository referenceGroupTypeRepository;

    @Override
    public ReferenceGroupType save(ReferenceGroupType groupType) {
        return referenceGroupTypeRepository.save(groupType);
    }

    @Override
    public Page<ReferenceGroupType> findAll(Pageable page) {
        Pageable toSend = page;
        if (toSend == null) {
            toSend = new PageRequest(0, 10, Sort.Direction.ASC, "id");
        }
        return referenceGroupTypeRepository.findAll(toSend);
    }

    @Override
    public ReferenceGroupType findByName(String groupTypeName) {
        return referenceGroupTypeRepository.findByName(groupTypeName);
    }

}
