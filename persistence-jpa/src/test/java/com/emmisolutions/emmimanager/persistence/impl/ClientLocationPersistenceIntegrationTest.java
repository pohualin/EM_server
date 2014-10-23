package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientLocationPersistence;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.LocationPersistence;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Test persistence layer for ClientLocation objects
 */
public class ClientLocationPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    ClientPersistence clientPersistence;

    @Resource
    LocationPersistence locationPersistence;

    @Resource
    ClientLocationPersistence clientLocationPersistence;

    /**
     * This test ensures that we can add locations to clients then
     * find them after saving.
     */
    @Test
    public void addLocationToClient() {
        Client client = makeClient();
        Location location = makeLocation();
        ClientLocation clientLocation =
                clientLocationPersistence.create(
                        new Location(location.getId(), location.getVersion()),
                        new Client(client.getId(), client.getVersion())
                );
        assertThat("client location is not null", clientLocation, is(notNullValue()));
        assertThat("client location has an id", clientLocation.getId(), is(notNullValue()));

        Page<ClientLocation> clientLocationPage = clientLocationPersistence.find(client, null);
        assertThat("client location is on the page", clientLocationPage, hasItem(clientLocation));
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void invalidFindCall(){
        clientLocationPersistence.find(null, null);
    }

    private Client makeClient() {
        Client client = new Client();
        client.setTier(new ClientTier(3l));
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setContractStart(LocalDate.now());
        client.setRegion(new ClientRegion(1l));
        client.setName("clientLocationPersistenceIntegrationTestClient " + System.currentTimeMillis());
        client.setType(new ClientType(1l));
        client.setActive(true);
        client.setContractOwner(new User(1l, 0));
        client.setSalesForceAccount(new SalesForce("clpit" + System.currentTimeMillis()));
        return clientPersistence.save(client);
    }

    private Location makeLocation() {
        Location location = new Location();
        location.setName("Valid Name 1");
        location.setCity("Valid City 1");
        location.setPhone("630-222-8900");
        location.setState(State.IL);
        return locationPersistence.save(location);
    }

}
