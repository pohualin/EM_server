package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.service.ClientService;
import org.joda.time.LocalDate;
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

    private Client makeClient(long i) {
        Client client = new Client();
        client.setId(i);
        client.setActive(i % 2 == 0);
        client.setName("Demo hospital client " + i);
        client.setType(ClientType.PROVIDER);
        client.setRegion(ClientRegion.NORTHEAST);
        client.setTier(ClientTier.THREE);
        User user = new User();
        user.setFirstName("contract owner");
        user.setLastName("" + i);
        client.setContractOwner(user);
        client.setContractStart(LocalDate.now());
        client.setContractEnd(LocalDate.now().plusYears(2));
        SalesForce salesForceAccount = new SalesForce();
        salesForceAccount.setId(i);
        salesForceAccount.setAccountNumber("" + System.currentTimeMillis());
        salesForceAccount.setName("SalesForce account " + i);
        client.setSalesForceAccount(salesForceAccount);
        client.setVersion((int)i);
        return client;
    }

    @Override
    public Client reload(Client client) {
        return makeClient(client.getId());
    }

    @Override
    public Client create(Client client) {
        client.setId(System.currentTimeMillis());
        client.setVersion(1);
        return reload(client);
    }
}
