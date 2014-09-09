package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientSearchFilter;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ClientRepository;
import com.emmisolutions.emmimanager.persistence.repo.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import static com.emmisolutions.emmimanager.persistence.impl.specification.ClientSpecifications.hasNames;
import static com.emmisolutions.emmimanager.persistence.impl.specification.ClientSpecifications.isInStatus;
import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * Client persistence implementation.
 */
@Repository
public class ClientPersistenceImpl implements ClientPersistence {

    @Resource
    ClientRepository clientRepository;

    @Resource
    UserRepository userRepository;

    @Override
    public Page<Client> list(Pageable page, ClientSearchFilter searchFilter) {
        if (page == null){
            // default pagination request if none
            page = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        }
        return clientRepository.findAll(where(hasNames(searchFilter)).and(isInStatus(searchFilter)), page);
    }

    @Override
    public Client save(Client client) {
    	if (client.getName()!=null) {
    		client.setNormalizedName(client.getName().toLowerCase()) ; //the normalized is used only for search client
    	}
        return clientRepository.save(client);
    }

    @Override
    public Client reload(Long id) {
        return clientRepository.findOne(id);
    }
    
    @Override
    public Client findByNormalizedName(String normalizedName){
    	//if (String.isNotBlank(normalizedName))
    	normalizedName = normalizedName == null? "": normalizedName;
    	normalizedName = normalizedName.toLowerCase(); //always change to lower case
    	return clientRepository.findByNormalizedName(normalizedName);
    }
}
