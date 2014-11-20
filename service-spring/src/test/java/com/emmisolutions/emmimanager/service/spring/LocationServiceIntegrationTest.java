package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.persistence.LocationPersistence;
import com.emmisolutions.emmimanager.persistence.repo.UserRepository;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.LocationService;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Location service persistence test
 */
public class LocationServiceIntegrationTest extends BaseIntegrationTest {

    @Resource
    LocationService locationService;

    @Resource
    LocationPersistence locationPersistence;

    @Resource
    ClientService clientService;

    @Resource
    UserRepository userService;

    UserAdmin user;

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
    public void list() {
        for (int i = 0; i < 200; i++) {
            locationPersistence.save(makeLocation("Valid Location", i));
        }
        Page<Location> locationPage = locationService.list(null);
        assertThat("there are 200 items", locationPage.getTotalElements() >= 200, is(true));

        locationPage = locationService.list(new PageRequest(1, 10),
                new LocationSearchFilter(LocationSearchFilter.StatusFilter.ACTIVE_ONLY, "valid", "name"));
        assertThat("there are 100 items", locationPage.getTotalElements() <= 200, is(true));

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
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void createRequiredFields() {
        locationService.create(null);
    }

    /**
     * Test update required fields
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void updateRequiredFields() {
        locationService.update(null, new Location());
    }

    /**
     * Trying to use non-persistent client in belongsTo should fail
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void updateBadClient() {
        Location location = locationService.create(makeLocation("matty12341234", 2));
        assertThat("location belongs to no client", location.getBelongsTo(), is(nullValue()));
        Client nonPersistentClient = new Client();
        location.setBelongsTo(nonPersistentClient);
        locationService.update(nonPersistentClient, location);
    }

    /**
     * Creation and association should happen in one method call
     */
    @Test
    public void fullCreate(){
        Client client = clientService.create(makeClient());
        Location location =  makeLocation("matty12341234", 2);
        location.setBelongsTo(client);
        location = locationService.create(client, location);
        assertThat("location belongs to the client", location.getBelongsTo(), is(client));
    }

    /**
     * Full creation with null Location should error out
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void fullCreateNoLocation() {
        locationService.create(null, null);
    }

    /**
     * Make sure that only the existing client that a location belongsTo can change
     * the belongsTo
     */
    @Test
    public void belongsToChange() {
        Client client = clientService.create(makeClient());
        Client client2 = clientService.create(makeClient());
        Location location = locationService.create(makeLocation("matty12341234", 2));
        location.setBelongsTo(client);
        location = locationService.update(client, location);
        assertThat("location belongs to the client", location.getBelongsTo(), is(client));

        // attempt to jump the belongsTo
        location.setBelongsTo(client2);
        location = locationService.update(client2, location);
        assertThat("location should still belong to the client", location.getBelongsTo(), is(client));

        // make sure we can null it out
        location.setBelongsTo(null);
        location = locationService.update(client, location);
        assertThat("location should be null", location.getBelongsTo(), is(nullValue()));

        // other client should be able to set it now
        location.setBelongsTo(client2);
        location = locationService.update(client2, location);
        assertThat("location should belong to client2", location.getBelongsTo(), is(client2));

        // try to change belongsTo with null client
        location.setBelongsTo(client);
        location = locationService.update(null, location);
        assertThat("location should still belong to client2", location.getBelongsTo(), is(client2));
    }

    /**
     * Normal update should work
     */
    @Test
    public void update() {
        Location location = locationService.create(makeLocation("matty12341234", 2));
        assertThat("location belongs to no client", location.getBelongsTo(), is(nullValue()));
        Client client = clientService.create(makeClient());
        location.setBelongsTo(client);
        locationService.update(client, location);
        assertThat("location belongs to client", location.getBelongsTo(), is(client));
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
