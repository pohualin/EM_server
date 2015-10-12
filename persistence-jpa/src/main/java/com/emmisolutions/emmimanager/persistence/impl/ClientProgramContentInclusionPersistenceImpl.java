package com.emmisolutions.emmimanager.persistence.impl;

import static org.springframework.data.jpa.domain.Specifications.where;

import com.emmisolutions.emmimanager.model.configuration.ClientProgramContentInclusion;
import com.emmisolutions.emmimanager.persistence.ClientProgramContentInclusionPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ClientProgramContentInclusionRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Persistence implementation for ClientProgramContentInclusionRepository entity.
 */
@Repository
public class ClientProgramContentInclusionPersistenceImpl implements
				ClientProgramContentInclusionPersistence {

    @Resource
    ClientProgramContentInclusionRepository clientProgramContentInclusionRepository;
    
    @Override
	public Page<ClientProgramContentInclusion> findByClient(Long clientId, Pageable pageable){
    	if (clientId == null) {
    		 return null;
        }
        return clientProgramContentInclusionRepository.findByClientId(clientId,
                pageable != null ? pageable : new PageRequest(0, 10, Sort.Direction.ASC, "id"));          
    }

	@Override
	public ClientProgramContentInclusion saveOrUpdate(
			ClientProgramContentInclusion clientProgramContentInclusion) {
		return clientProgramContentInclusionRepository.save(clientProgramContentInclusion);
	}

	@Override
	public ClientProgramContentInclusion reload(Long id) {
		 if (id == null) {
	            return null;
	    }
		return clientProgramContentInclusionRepository.findOne(id);
	}

	@Override
	public void delete(Long id) {
		clientProgramContentInclusionRepository.delete(id);	
	}

}
