package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.SalesForceService;
import com.emmisolutions.emmimanager.service.UserService;
import org.joda.time.LocalDate;
import org.junit.Test;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
public class SalesForceLookupServiceImplTest extends BaseIntegrationTest {

    @Resource
    SalesForceService salesForceService;

    @Resource
    ClientService clientService;

    @Resource
    UserService userService;

    @Test
    public void find(){
        String accountNumber = "0013000000CqY7OAAV";

        SalesForceSearchResponse salesForceSearchResponse = salesForceService.find(accountNumber);
        assertThat("response has a result", salesForceSearchResponse.getTotal(), is(1));
        assertThat("response does not have an id", salesForceSearchResponse.getAccounts().get(0).getId(), is(nullValue()));


        SalesForce sf = clientService.create(makeClient("clientName", "sfUser", accountNumber)).getSalesForceAccount();
        salesForceSearchResponse = salesForceService.find(accountNumber);
        assertThat("response has same id as persisted account", salesForceSearchResponse.getAccounts().get(0).getId(), is(sf.getId()));
        assertThat("response has same version as persisted account", salesForceSearchResponse.getAccounts().get(0).getVersion(), is(sf.getVersion()));
        assertThat("client name is correct", salesForceSearchResponse.getAccounts().get(0).getClient().getName(), is("clientName"));
    }

    private Client makeClient(String clientName, String username, String accountNumber){
        Client client = new Client();
        client.setType(ClientType.PROVIDER);
        client.setContractStart(LocalDate.now());
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setName(clientName);
        client.setContractOwner(userService.save(new User(username, "pw")));
        client.setSalesForceAccount(new SalesForce(accountNumber, client));
        return client;
    }
}