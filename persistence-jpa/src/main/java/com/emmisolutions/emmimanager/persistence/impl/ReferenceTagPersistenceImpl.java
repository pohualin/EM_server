package com.emmisolutions.emmimanager.persistence.impl;

import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.emmisolutions.emmimanager.persistence.ReferenceTagPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ReferenceGroupRepository;
import com.emmisolutions.emmimanager.persistence.repo.ReferenceTagRepository;

/**
 *  ReferenceTagPersistence implementation
 */
@Repository
public class ReferenceTagPersistenceImpl implements ReferenceTagPersistence {

@Resource
ReferenceTagRepository referenceTagRepository;
@Resource
ReferenceGroupRepository referenceGroupRepository;

    @Override
	public ReferenceTag save(ReferenceTag tag){
	    return referenceTagRepository.save(tag);
	}
    
    @Override
    public Set<ReferenceTag> findAllByGroup(ReferenceGroup group){
        return referenceTagRepository.findAllByGroup(group);
    }
}
