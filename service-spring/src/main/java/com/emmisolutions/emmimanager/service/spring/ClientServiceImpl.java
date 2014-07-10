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
        int hardCeiling = 202;
        int max = (pageable.getOffset() + pageable.getPageSize());
        if (max > hardCeiling) {
            max = hardCeiling;
        }
        for (int i = pageable.getOffset(); i < max; i++) {
            clients.add(makeClient(i + 1));
        }
        if (pageable.getOffset() >= max) {
            clients.add(makeClient(max));
        }
        return new PageImpl<>(clients, pageable, hardCeiling);
    }

    private Client makeClient(int i) {
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

        return client;
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
