package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.persistence.LocationPersistence;
import com.emmisolutions.emmimanager.persistence.repo.UserRepository;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.LocationService;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Location service persistence test
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocationServiceIntegrationTest extends BaseIntegrationTest {

    @Resource
    LocationService locationService;

    @Resource
    LocationPersistence locationPersistence;

    @Resource
    ClientService clientService;

    @Resource
    UserRepository userService;

    User user;

    /**
     * Save a user to the db
     *
     * @return
     */
    @Before
    public void init() {
        user = userService.fetchWithFullPermissions("super_admin");
    }

    /**
     * List locations
     */
    @Test
    public void aList() {
        for (int i = 0; i < 200; i++) {
            locationPersistence.save(makeLocation("Valid Location", i));
        }
        Page<Location> locationPage = locationService.list(null, null);
        assertThat("there are 200 items", locationPage.getTotalElements(), is(200l));

        locationPage = locationService.list(new PageRequest(1, 10),
                new LocationSearchFilter(LocationSearchFilter.StatusFilter.ACTIVE_ONLY, "valid", "name"));
        assertThat("there are 100 items", locationPage.getTotalElements(), is(100l));

    }

    /**
     * Test reloadLocationUsingClient
     */
    @Test
    public void reload() {
        Location saved = locationService.create(makeLocation("for reloadLocationUsingClient", 0));
        Location another = locationService.reload(saved);

        assertThat("reloaded location is the same", saved, is(another));
    }

    /**
     * Test create required fields
     */
    @Test(expected = IllegalArgumentException.class)
    public void createRequiredFields() {
        locationService.create(null);
    }

    /**
     * Test update required fields
     */
    @Test(expected = IllegalArgumentException.class)
    public void updateRequiredFields() {
        locationService.update(new Location());
    }

    /**
     * Test update.
     */
    @Test
    public void update() {
        Client belongsTo = clientService.create(makeClient());
        Client using = clientService.create(makeClient());
        Location unsaved = makeLocation("for update", 2);
        unsaved.addClientUsingThisLocation(using);
        Location saved = locationService.create(unsaved);
        saved.setBelongsTo(belongsTo);

        locationService.update(saved);

        Page<Location> locationPage = locationService.list(new LocationSearchFilter(using.getId(), LocationSearchFilter.StatusFilter.ACTIVE_ONLY, "for update"));
        assertThat("should not find anything because update and create should not set the belongs to dependencies", locationPage.getContent().size(), is(0));
    }

    /**
     * This test ensures we can remove a single location from a client, but doesn't use
     * the ClientLocationModificationRequest to accomplish this
     */
    @Test
    public void removeSingleLocationFromClient() {
        Client client = clientService.create(makeClient());
        String locationName = "" + System.currentTimeMillis();
        Location unsaved = makeLocation(locationName, 2);
        final Location location = locationService.create(unsaved);

        ClientLocationModificationRequest modificationRequest = new ClientLocationModificationRequest();
        modificationRequest.setAdded(new ArrayList<Location>() {{
            add(location);
        }});
        modificationRequest.setBelongsToUpdated(new ArrayList<Location>(){{
            add(location);
        }});
        locationService.updateClientLocations(client, modificationRequest);

        Location populatedLocation = locationService.reloadLocationUsedByClient(client, location);
        assertThat("location is same", populatedLocation, is(location));

        locationService.delete(client, location);
        populatedLocation = locationService.reloadLocationUsedByClient(client, location);
        assertThat("location is doesn't come back anymore", populatedLocation, is(nullValue()));

        populatedLocation = locationService.reload(location);
        assertThat("belongs to relationship wasn't removed", populatedLocation.getBelongsTo(), is(client));
    }

    /**
     * Test that the client locations update required fields
     */
    @Test
    public void updateClientLocationsNoWork() {
        locationService.updateClientLocations(new Client(), new ClientLocationModificationRequest());
        // no exception should be thrown
    }

    @Test
    public void updateClientLocations() {
        Client client = clientService.create(makeClient());
        final Location one = locationService.create(makeLocation("locations-", 1));
        final Location two = locationService.create(makeLocation("locations-", 2));
        ClientLocationModificationRequest modificationRequest = new ClientLocationModificationRequest();
        modificationRequest.setAdded(new ArrayList<Location>() {{
            add(one);
            add(two);
        }});
        modificationRequest.setBelongsToUpdated(new ArrayList<Location>());
        locationService.updateClientLocations(client, modificationRequest);
        assertThat("locations have a client", locationService.reloadLocationUsedByClient(client, one), is(notNullValue()));

        modificationRequest.setDeleted(new ArrayList<Location>() {{
            add(one);
        }});
        locationService.updateClientLocations(client, modificationRequest);
        assertThat("delete takes precedence over add, one should not have client", locationService.reloadLocationUsedByClient(client, one), is(nullValue()));

        one.setBelongsTo(client);
        modificationRequest.setBelongsToUpdated(new ArrayList<Location>() {{
            add(one);
        }});
        locationService.updateClientLocations(client, modificationRequest);
        Page<Location> locationPage = locationService.list(new LocationSearchFilter("locations"));
        assertThat("location should still be one", locationPage.getContent().get(0), is(one));
        assertThat("belongs to should be correct", locationPage.getContent().get(0).getBelongsTo(), is(client));

        // attempt to update belongsTo from different client
        Client anotherClient = clientService.create(makeClient());
        one.setBelongsTo(anotherClient);
        modificationRequest = new ClientLocationModificationRequest();
        modificationRequest.setBelongsToUpdated(new ArrayList<Location>() {{
            add(one);
        }});
        locationService.updateClientLocations(anotherClient, modificationRequest);
        locationPage = locationService.list(new LocationSearchFilter("locations"));
        assertThat("should still belong to original client", locationPage.getContent().get(0).getBelongsTo(), is(client));

        //set belongsTo to null
        one.setBelongsTo(null);
        locationService.updateClientLocations(client, modificationRequest);
        locationPage = locationService.list(new LocationSearchFilter("locations"));
        assertThat("client should be null", locationPage.getContent().get(0).getBelongsTo(), is(nullValue()));

        // set it to another client
        one.setBelongsTo(anotherClient);
        locationService.updateClientLocations(anotherClient, modificationRequest);
        locationPage = locationService.list(new LocationSearchFilter("locations"));
        assertThat("client should be another client, not the first", locationPage.getContent().get(0).getBelongsTo(), is(anotherClient));

    }

    /**
     * Test list by client that has using relationships
     */
    @Test
    @Transactional // only necessary because we want to traverse the 'using' relationship via a search
    public void listByClientWithUsingRelationships() {
        Client one = clientService.create(makeClient());
        Client three = clientService.create(makeClient());

        final Location oneLocation = makeLocation("BY_CLIENT-", 1);

        // create using relationship one client, three client --> one location
        ClientLocationModificationRequest oneClientUsingModRequest = new ClientLocationModificationRequest();
        oneClientUsingModRequest.setAdded(new ArrayList<Location>() {{
            add(locationService.create(oneLocation));
        }});
        locationService.updateClientLocations(one, oneClientUsingModRequest);
        locationService.updateClientLocations(three, oneClientUsingModRequest);

        Page<Location> locationPage = locationService.list(new LocationSearchFilter("BY_CLIENT"));
        assertThat("Should only be one locations in the page", locationPage.getTotalElements(), is(1l));
        assertThat("There should be two clients using this location",
                locationPage.getContent().get(0).getUsingThisLocation(), hasItems(one, three));

        assertThat("reverse way should also work.. ID fetch by client has the location id", locationService.list(one.getId()), hasItem(oneLocation.getId()));
    }

    private Client makeClient() {
        Client client = new Client();
        client.setType(new ClientType(2l));
        client.setContractStart(LocalDate.now());
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setName("whatever" + System.currentTimeMillis());
        client.setContractOwner(user);
        client.setSalesForceAccount(new SalesForce("xxxWW" + System.currentTimeMillis()));
        return client;
    }

    private Location makeLocation(String name, long i) {
        Location location = new Location();
        String suffix = encode(i);
        location.setName(name + suffix);
        location.setCity("Valid City " + suffix);
        location.setActive(i % 2 == 0);
        location.setPhone("555-422-1212");
        location.setState(State.IL);
        return location;
    }

    private String encode(long i) {
        StringBuilder sb = new StringBuilder();
        String num = Long.toString(i);
        for (int j = 0; j < num.length(); j++) {
            int c = num.charAt(j) + 17;
            sb.append((char) c);
        }
        return sb.toString();
    }
}
