package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.UserAdminPersistence;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.SalesForceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * Implementation of the ClientService
 */
@Service
public class ClientServiceImpl implements ClientService {

    @Resource
    ClientPersistence clientPersistence;

    @Resource
    UserAdminPersistence userAdminPersistence;

    @Resource
    SalesForceService salesForceService;

    @Override
    @Transactional(readOnly = true)
    public Page<Client> list(Pageable pageable, ClientSearchFilter searchFilter) {
        return clientPersistence.list(pageable, searchFilter);
    }

    @Override
    public Page<Client> list(ClientSearchFilter searchFilter) {
        return list(null, searchFilter);
    }

    @Override
    @Transactional(readOnly = true)
    public Client reload(Client client) {
        return clientPersistence.reload(client);
    }

    @Override
    @Transactional
    public Client create(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("client cannot be null");
        }
        client.setId(null);
        client.setVersion(null);
        updateSalesForceDetails(client.getSalesForceAccount());
        return clientPersistence.save(client);
    }

    @Override
    @Transactional
    public Client update(Client client) {
        Client dbClient = clientPersistence.reload(client);
        if (dbClient == null) {
            throw new IllegalArgumentException("This method can only be used with an existing persistent client.");
        }
        // allow for null SF object to come in, use the existing persistent SF object in this case
        SalesForce toBeUpdated = client.getSalesForceAccount() != null ?
                client.getSalesForceAccount() : dbClient.getSalesForceAccount();

        // make sure the same SF object is used for the update, do not create a new SF object on each save
        toBeUpdated.setId(dbClient.getSalesForceAccount().getId());
        toBeUpdated.setVersion(dbClient.getSalesForceAccount().getVersion());
        toBeUpdated.setClient(client);
        client.setSalesForceAccount(toBeUpdated);

        // update the SF object with the latest from sf
        updateSalesForceDetails(toBeUpdated);

        // update the client
        return clientPersistence.save(client);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserAdmin> listPotentialContractOwners(Pageable pageable) {
        return userAdminPersistence.listPotentialContractOwners(pageable);
    }

	@Override
	public Client findByNormalizedName(String normalizedName) {
		return clientPersistence.findByNormalizedName(normalizedName);
	}

    @Override
    public Collection<ClientType> getAllClientTypes() {
        return clientPersistence.getAllClientTypes();
    }

    @Override
    public Collection<ClientRegion> getAllClientRegions() {
        return clientPersistence.getAllRegionTypes();
    }

    @Override
    public Collection<ClientTier> getAllClientTiers() {
        return clientPersistence.getAllClientTiers();
    }

    private void updateSalesForceDetails(SalesForce clientSalesForce) {
        if (clientSalesForce != null && StringUtils.isNotBlank(clientSalesForce.getAccountNumber())) {
            SalesForce sf = salesForceService.findAccountById(clientSalesForce.getAccountNumber());
            if (sf != null) {
                clientSalesForce.setCity(sf.getCity());
                clientSalesForce.setCountry(sf.getCountry());
                clientSalesForce.setName(sf.getName());
                clientSalesForce.setPhoneNumber(sf.getPhoneNumber());
                clientSalesForce.setPostalCode(sf.getPostalCode());
                clientSalesForce.setState(sf.getState());
                clientSalesForce.setStreet(sf.getStreet());
            }
        }
    }
}

