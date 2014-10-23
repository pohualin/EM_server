package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.*;
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
     * Update to non-persistent client should fail (no cascading should be allowed)
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void updateBadClient() {
        Location location = locationService.create(makeLocation("matty12341234", 2));
        assertThat("location belongs to no client", location.getBelongsTo(), is(nullValue()));
        location.setBelongsTo(new Client());
        locationService.update(location);
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
        locationService.update(location);
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
