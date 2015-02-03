package com.emmisolutions.emmimanager.persistence.impl;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Page<ReferenceTag> findAllByGroup(ReferenceGroup group, Pageable page){
        if (page == null) {
            // default pagination request if none
            page = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        }
        return referenceTagRepository.findAllByGroup(group, page);
    }
    
    @Override
    public ReferenceTag reload(ReferenceTag tag){
        if (tag.getId() == null){
            return null;
        }
        return referenceTagRepository.findOne(tag.getId());
    }
        
}
