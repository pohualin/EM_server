package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientLocationService;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.LocationService;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
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
        Set<ClientLocation> savedLocations = clientLocationService.create(client, new HashSet<Location>() {{
            add(location);
        }});
        assertThat("client has one location", savedLocations.size(), is(1));
        assertThat("location is the one we added", savedLocations.iterator().next().getLocation(), is(location));

        ClientLocation clientLocation = clientLocationService.reload(savedLocations.iterator().next());
        assertThat("client location was loaded", clientLocation, is(notNullValue()));

        clientLocationService.remove(clientLocation);
        assertThat("client has zero locations after delete", 0l, is(clientLocationService.find(client, null).getTotalElements()));
    }

    /**
     * Ensure that finding possible locations works.. including making sure
     * existing locations function properly.
     */
    @Test
    public void findPossibleLocations(){
        // make a bunch of locations
        final Location location = makeLocation();
        for (int i = 0; i < 10; i++) {
            makeLocation();
        }

        // associate a client to one of those locations
        Client client = makeClient();
        Set<ClientLocation> savedLocations = clientLocationService.create(client, new HashSet<Location>() {{
            add(location);
        }});
        ClientLocation savedRelationship = savedLocations.iterator().next();

        // find a page of possible ClientLocations using the same name that we used during create
        Page<ClientLocation> possibleLocations =
                clientLocationService.findPossibleLocationsToAdd(client, new LocationSearchFilter("ClientLocationServiceIntegrationTest"), null);
        assertThat("there should be 11 locations found", possibleLocations.getTotalElements(), is(11l));
        assertThat("one of the ClientLocation objects should be the one we saved", possibleLocations, hasItem(savedRelationship));
    }

    /**
     * Creates a location on a client (the ClientLocation)
     */
    @Test
    public void createLocationOnClient(){
        Client client = makeClient();
        Location location = new Location();
        location.setName("Brand New Location");
        location.setCity("Valid City 1");
        location.setPhone("630-222-8900");
        location.setState(State.IL);
        location.setBelongsTo(client);

        ClientLocation clientLocation = clientLocationService.createLocationAndAssociateTo(client, location);
        assertThat("ClientLocation is not null", clientLocation, is(notNullValue()));

        Page<ClientLocation> clientLocationPage = clientLocationService.find(client, null);
        assertThat("finding client locations should include the newly created one", clientLocationPage, hasItem(clientLocation));
        assertThat("Belongs To is correct", clientLocationPage.iterator().next().getLocation().getBelongsTo(), is(client));
    }

    /**
     * Can't find with a null client
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void invalidFindCall(){
        clientLocationService.find(null, null);
    }

    /**
     * Can't delete with a null client location
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void invalidDeleteCall(){
        clientLocationService.remove(null);
    }

    /**
     * Can't reload a null client location
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void invalidReloadCall(){
        clientLocationService.reload(null);
    }

    /**
     * Can't find possible locations for a null client
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void invalidFindPossibleCall(){
        clientLocationService.findPossibleLocationsToAdd(null, null, null);
    }

    /**
     * Test invalid api access for create
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void invalidCreate(){
         clientLocationService.create(null, null);
    }


    private Client makeClient() {
        Client client = new Client();
        client.setTier(new ClientTier(3l));
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setContractStart(LocalDate.now());
        client.setRegion(new ClientRegion(1l));
        client.setName(RandomStringUtils.randomAlphanumeric(18));
        client.setType(new ClientType(1l));
        client.setActive(true);
        client.setContractOwner(new User(1l, 0));
        client.setSalesForceAccount(new SalesForce(RandomStringUtils.randomAlphanumeric(18)));
        return clientService.create(client);
    }

    private Location makeLocation() {
        Location location = new Location();
        location.setName("ClientLocationServiceIntegrationTest" + RandomStringUtils.randomAlphanumeric(18));
        location.setCity("Valid City 1");
        location.setPhone("630-222-8900");
        location.setState(State.IL);
        return locationService.create(location);
    }
}
