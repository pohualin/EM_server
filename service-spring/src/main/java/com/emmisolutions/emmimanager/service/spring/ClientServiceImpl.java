package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Salesforce;
import com.emmisolutions.emmimanager.service.ClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the ClientService
 */
@Service
public class ClientServiceImpl implements ClientService {

    @Override
    public Page<Client> list(Pageable pageable) {
        List<Client> clients = new ArrayList<>();
        for (int i = pageable.getOffset(); i < (pageable.getOffset() + pageable.getPageSize()); i++) {
            Client client = new Client();
            client.setId((long) i);
            client.setActive(true);
            client.setName("Demo hospital client " + i);
            client.setType("Hospital " + i);
            client.setRegion("Region " + i);
            client.setOwner("Owner " + i);
            Salesforce salesForceAccount = new Salesforce();
            salesForceAccount.setId((long) i);
            salesForceAccount.setName("SalesForce account " + i);
            client.setSalesForceAccount(salesForceAccount);
            client.setVersion(i);
            clients.add(client);
        }
        return new PageImpl<>(clients, pageable, 200);
    }

    @Override
    public Client reload(Client client) {
        Client ret = new Client();
        ret.setId(client.getId());
        ret.setActive(true);
        ret.setName("Demo hospital client");
        ret.setType("Hospital");
        ret.setRegion("Region");
        ret.setOwner("Owner");
        return ret;
    }
}
