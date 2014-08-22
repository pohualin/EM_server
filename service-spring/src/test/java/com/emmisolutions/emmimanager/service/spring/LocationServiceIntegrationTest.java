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
     * Test reload
     */
    @Test
    public void reload() {
        Location saved = locationService.create(makeLocation("for reload", 0));
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
     * Test update. Need Transactional annotation here for some reason
     * it has to do with hibernate flushing things after the update() call.
     * If you take it off the test will fail on the assertions of the locationPage
     */
    @Test
    @Transactional
    public void update() {
        Client belongsTo = clientService.create(makeClient());
        Client using = clientService.create(makeClient());
        Location unsaved = makeLocation("for update", 2); // even ids are active
        unsaved.addClientUsingThisLocation(using);
        Location saved = locationService.create(unsaved);
        saved.setBelongsTo(belongsTo);
        saved = locationService.update(saved);
        Page<Location> locationPage = locationService.list(new LocationSearchFilter(using.getId(), LocationSearchFilter.StatusFilter.ACTIVE_ONLY, "for update"));
        assertThat("found it by client", locationPage.getContent(), hasItem(saved));
        assertThat("belongs to came back properly", locationPage.getContent().get(0).getBelongsTo(), is(belongsTo));

    }

    /**
     * Test that the client locations update required fields
     */
    @Test
    public void updateClientLocationsNoWork(){
        locationService.updateClientLocations(new Client(), new ClientLocationModificationRequest());
        // no exception should be thrown
    }

    @Test
    public void updateClientLocations() {
        Client client = clientService.create(makeClient());
        final Location one = locationService.create(makeLocation("locations-", 1));
        final Location two = locationService.create(makeLocation("locations-", 2));
        ClientLocationModificationRequest modificationRequest = new ClientLocationModificationRequest();
        modificationRequest.setAdded(new ArrayList<Location>(){{
            add(one);
            add(two);
        }});
        locationService.updateClientLocations(client, modificationRequest);
        Page<Location> locationPage = locationService.list(new LocationSearchFilter("locations"));
        assertThat("locations have a client", locationPage.getContent().get(1).getUsingThisLocation(), hasItem(client));

        modificationRequest.setDeleted(new ArrayList<Location>(){{
            add(one);
        }});
        locationService.updateClientLocations(client, modificationRequest);
        locationPage = locationService.list(new LocationSearchFilter("locations"));
        Location shouldBeOne = locationPage.getContent().get(0);
        assertThat("location is one", shouldBeOne, is(one));
        assertThat("delete takes precedence over add, one should not have client", shouldBeOne.getUsingThisLocation(), not(is(hasItem(client))));

    }

    /**
     * Test search by client
     */
    @Test
    public void byClient() {
        Client one = makeClient();
        one = clientService.create(one);
        Client two = makeClient();
        two = clientService.create(two);
        Client three = makeClient();
        three = clientService.create(three);

        Location oneLocation = makeLocation("BY_CLIENT-", one.getId());
        Location twoLocation = makeLocation("BY_CLIENT-", two.getId());
        oneLocation.addClientUsingThisLocation(one);
        oneLocation.addClientUsingThisLocation(three);
        twoLocation.addClientUsingThisLocation(two);

        oneLocation = locationService.create(oneLocation);
        twoLocation = locationService.create(twoLocation);

        Page<Location> locationPage = locationService.list(new LocationSearchFilter("BY_CLIENT"));
        assertThat("Should only be two locations in the page", locationPage.getTotalElements(), is(2l));

        for (Location location : locationPage) {
            if (location.equals(oneLocation)) {
                assertThat("There should be two clients using this location", location.getUsingThisLocation(), hasItems(one, three));
                assertThat("two should not be using this location", location.getUsingThisLocation(), is(not(hasItems(two))));
            }
            if (location.equals(twoLocation)) {
                assertThat("one and three should not be using this location", location.getUsingThisLocation(), is(not(hasItems(one, three))));
                assertThat("two should be using this location", location.getUsingThisLocation(), hasItem(two));
            }
        }

        assertThat("ID fetch by client has the location id", locationService.list(one.getId()), hasItem(oneLocation.getId()));
    }

    private Client makeClient() {
        Client client = new Client();
        client.setType(ClientType.PROVIDER);
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
        location.setPhone("phone number" + suffix);
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
