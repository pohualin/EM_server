package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientLocationService;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.LocationService;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.data.domain.Page;

import javax.annotation.Resource;
import java.util.HashSet;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Integration test for the ClientLocationService API
 */
public class ClientLocationServiceIntegrationTest extends BaseIntegrationTest {

    @Resource
    ClientService clientService;

    @Resource
    LocationService locationService;

    @Resource
    ClientLocationService clientLocationService;

    /**
     * This test is the lifecycle of a ClientLocation. It creates one, finds it, then deletes it.
     */
    @Test
    public void createFindDelete() {
        final Location location = makeLocation();
        Client client = makeClient();
        Page<ClientLocation> clientLocationPage = clientLocationService.create(client, new HashSet<Location>() {{
            add(location);
        }}, null);
        assertThat("client has one location", clientLocationPage.getTotalElements(), is(1l));
        assertThat("location is the one we added", clientLocationPage.iterator().next().getLocation(), is(location));

        clientLocationService.remove(clientLocationPage.iterator().next());
        assertThat("client has zero locations after delete", 0l, is(clientLocationService.find(client, null).getTotalElements()));
    }


    private Client makeClient() {
        Client client = new Client();
        client.setTier(new ClientTier(3l));
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setContractStart(LocalDate.now());
        client.setRegion(new ClientRegion(1l));
        client.setName("ClientLocationServiceIntegrationTest " + System.currentTimeMillis());
        client.setType(new ClientType(1l));
        client.setActive(true);
        client.setContractOwner(new User(1l, 0));
        client.setSalesForceAccount(new SalesForce("clsit" + System.currentTimeMillis()));
        return clientService.create(client);
    }

    private Location makeLocation() {
        Location location = new Location();
        location.setName("Valid Name 1");
        location.setCity("Valid City 1");
        location.setPhone("630-222-8900");
        location.setState(State.IL);
        return locationService.create(location);
    }
}
