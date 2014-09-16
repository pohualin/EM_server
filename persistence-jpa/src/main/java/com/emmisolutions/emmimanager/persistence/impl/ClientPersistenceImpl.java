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
import org.apache.commons.lang3.*;
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

    public Client save(Client client) {
    	client.setNormalizedName(normalizeName(client)); 
        return clientRepository.save(client);
    }
    
    @Override
    public Client reload(Long id) {
        return clientRepository.findOne(id); 
    }
    
    @Override
    public Client findByNormalizedName(String normalizedName){		
    	String toSearch = normalizeName(normalizedName);
    	Client ret = null;
    	if (StringUtils.isNotBlank(toSearch)){
    	   ret = clientRepository.findByNormalizedName(toSearch);
    	}
    	return ret;
    }

    /**
     * remove the special characters replacing it with blank (" ") and change all to lower case
     * 
     * @param name
     * @return
     */
    private String normalizeName(String name) {
    	String normalizedName = StringUtils.trimToEmpty(StringUtils.lowerCase(name));
    	if (StringUtils.isNotBlank(normalizedName)){
    	    // do regex
    	    normalizedName = normalizedName.replaceAll("[^a-z0-9 ]*","");
    	}
    	return normalizedName;
    }
    
    private String normalizeName(Client client){    	
    	return normalizeName(client.getName()==null?"":client.getName());
    }
    
}
