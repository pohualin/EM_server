package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.persistence.ReferenceGroupPersistence;
import com.emmisolutions.emmimanager.persistence.impl.specification.ReferenceGroupSpecifications;
import com.emmisolutions.emmimanager.persistence.repo.ReferenceGroupRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 *  ReferenceGroupPersistence implementation
 */
@Repository
public class ReferenceGroupPersistenceImpl implements ReferenceGroupPersistence {

	@Resource
	ReferenceGroupRepository referenceGroupRepository;
	
	@Resource
	ReferenceGroupSpecifications referenceGroupSpecifications;

    @Override
    public Page<ReferenceGroup> loadReferenceGroups(Pageable page) {
        if (page == null) {
            // default pagination request if none
            page = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        }
        return referenceGroupRepository.findAll(page);
    }
    
    @Override
    public Page<ReferenceGroup> loadActiveReferenceGroups(Pageable page) {
        if (page == null) {
            // default pagination request if none
            page = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        }
        return referenceGroupRepository.findAll(
                where(referenceGroupSpecifications.isActive()), page);
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

    @Override
    public void delete(ReferenceGroup referenceGroup) {
        referenceGroupRepository.delete(referenceGroup);
    }
}
