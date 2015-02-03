package com.emmisolutions.emmimanager.persistence.impl;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.persistence.ReferenceGroupPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ReferenceGroupRepository;

/**
 *  ReferenceGroupPersistence implementation
 */
@Repository
public class ReferenceGroupPersistenceImpl implements ReferenceGroupPersistence {

	@Resource
	ReferenceGroupRepository referenceGroupRepository;

    @Override
    public Page<ReferenceGroup> loadReferenceGroups(Pageable page) {
        if (page == null) {
            // default pagination request if none
            page = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        }
        Page<ReferenceGroup> groups = referenceGroupRepository.findAll(page);
        return groups;
    }
	
	@Override
	public ReferenceGroup save(ReferenceGroup group){
	    return referenceGroupRepository.save(group);
	}
	
    @Override
    public ReferenceGroup reload(Long id) {
        if (id == null) {
            return null;
        }
        return referenceGroupRepository.findOne(id);
    }
}
