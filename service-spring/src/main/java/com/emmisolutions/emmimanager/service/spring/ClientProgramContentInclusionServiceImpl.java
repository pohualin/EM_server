package com.emmisolutions.emmimanager.service.spring;

import javax.annotation.Resource;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.ClientProgramContentInclusion;
import com.emmisolutions.emmimanager.persistence.ClientProgramContentInclusionPersistence;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.service.ClientProgramContentInclusionService;
import com.emmisolutions.emmimanager.service.ClientService;

/**
 * Service layer for ClientProgramContentInclusion
 * 
 */
@Service
public class ClientProgramContentInclusionServiceImpl implements
				ClientProgramContentInclusionService {

    @Resource
    ClientService clientService;

    @Resource
    ClientPersistence clientPersistence;

    @Resource
    ClientProgramContentInclusionPersistence ClientProgramContentInclusionPersistence;

    @Override
    @Transactional
	public ClientProgramContentInclusion update(
			ClientProgramContentInclusion clientProgramContentInclusion) {
		if (clientProgramContentInclusion == null
                || clientProgramContentInclusion.getId() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "ClientProgramContentInclusion or ClientProgramContentInclusionId can not be null.");
        }
		Client reload = clientService.reload(clientProgramContentInclusion
                .getClient());
		clientProgramContentInclusion.setClient(reload);
		return ClientProgramContentInclusionPersistence
                .saveOrUpdate(clientProgramContentInclusion);
	}

    @Override
    @Transactional
	public ClientProgramContentInclusion create(
			ClientProgramContentInclusion clientProgramContentInclusion) {
    	if((clientProgramContentInclusion.getClient() == null) &&
    	    (clientProgramContentInclusion.getClient().getId() == null)){
    		throw new InvalidDataAccessApiUsageException(
                    "ClientId can not be null for creating the client's content subscription.");
    	}
		Client reload = clientService.reload(clientProgramContentInclusion
                .getClient());
		clientProgramContentInclusion.setClient(reload);
        return ClientProgramContentInclusionPersistence
                .saveOrUpdate(clientProgramContentInclusion);
	}
    
    @Override
    @Transactional
	public void delete(
			ClientProgramContentInclusion clientProgramContentInclusion) {
    	if (clientProgramContentInclusion != null
                && clientProgramContentInclusion.getId() != null) {
    		ClientProgramContentInclusionPersistence.delete(clientProgramContentInclusion.getId());
       	}
    }

	@Override
	public Page<ClientProgramContentInclusion> findByClient(
			Client client, Pageable pageable) {
	   	return ClientProgramContentInclusionPersistence.findByClient(client.getId(), pageable);
  	}

	@Override
	public ClientProgramContentInclusion reload(
			ClientProgramContentInclusion clientProgramContentInclusion) {
		return ClientProgramContentInclusionPersistence
	                .reload(clientProgramContentInclusion.getId());
   	}
}
