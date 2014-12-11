package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientLocationPersistence;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.LocationPersistence;

import org.apache.commons.lang3.RandomStringUtils;
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
        Location location = makeLocation(1);
        ClientLocation clientLocation = clientLocationPersistence.create(location.getId(), client.getId());
        assertThat("client location is not null", clientLocation, is(notNullValue()));
        assertThat("client location has an id", clientLocation.getId(), is(notNullValue()));

        clientLocation = clientLocationPersistence.reload(clientLocation.getId());
        assertThat("client location is not null", clientLocation, is(notNullValue()));

        Page<ClientLocation> clientLocationPage = clientLocationPersistence.findByClient(client.getId(), null);
        assertThat("client location is on the page", clientLocationPage, hasItem(clientLocation));
        
        Page<ClientLocation> locationClientPage = clientLocationPersistence.findByLocation(location.getId(), null);
        assertThat("client location is on the page", locationClientPage, hasItem(clientLocation));

        clientLocationPersistence.remove(clientLocation.getId());
        assertThat("client location has been removed", clientLocationPersistence.reload(clientLocation.getId()), is(nullValue()));
    }
    
    /**
     * Reload of a null id should not error out
     */
    @Test
    public void testReloadOfNull(){
       assertThat("reload of null should yield null", clientLocationPersistence.reload(null), is(nullValue()));
    }

    /**
     * Make sure that potential locations for a client load properly
     */
    @Test
    public void findPotentialLocations(){
        // create a client
        Client client = makeClient();

        // create a bunch of locations
        Location location = makeLocation(900);
        for (int i = 0; i < 10; i++) {
             makeLocation(i);
        }
        // associate the Client to one of the locations
        ClientLocation clientLocation = clientLocationPersistence.create(location.getId(), client.getId());

        // find a page of Locations using the same name that we used during create
        Page<Location> savedLocations = locationPersistence.list(null, new LocationSearchFilter("Client Location Association"));
        assertThat("there should be 11 locations found", savedLocations.getTotalElements(), is(11l));

        // do the actual test
        assertThat("Load call should contain the persistent ClientLocation", clientLocationPersistence.load(client.getId(), savedLocations), hasItem(clientLocation));
    }

    /**
     * Empty load call should not error out
     */
    @Test
    public void emptyLoadCall(){
       assertThat("Empty list should come back", clientLocationPersistence.load(null, null).isEmpty(), is(true));
    }

    /**
     * Call the find method in a way that is not allowed
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void invalidFindCall(){
        clientLocationPersistence.findByClient(null, null);
    }

    private Client makeClient() {
        Client client = new Client();
        client.setTier(new ClientTier(3l));
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setContractStart(LocalDate.now());
        client.setRegion(new ClientRegion(1l));
        client.setName("clientLocationPersistenceIntegrationTestClient" + RandomStringUtils.randomAlphanumeric(18));
        client.setType(new ClientType(1l));
        client.setActive(true);
        client.setContractOwner(new UserAdmin(1l, 0));
        client.setSalesForceAccount(new SalesForce(RandomStringUtils.randomAlphanumeric(18)));
        return clientPersistence.save(client);
    }

    private Location makeLocation(int id) {
        Location location = new Location();
        location.setName("Client Location Association " + id);
        location.setCity("Valid City 1");
        location.setPhone("630-222-8900");
        location.setState(State.TX);
        return locationPersistence.save(location);
    }

}
