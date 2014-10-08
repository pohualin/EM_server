package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.LocationPersistence;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ClientTypeRepository;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Test class
 */
public class LocationPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    LocationPersistence locationPersistence;

    @Resource
    ClientPersistence clientPersistence;

    @Resource
    UserPersistence userPersistence;

    User superAdmin;

    @Resource
    ClientTypeRepository clientTypeRepository;

    ClientType clientType;

    @Before
    public void init() {
        superAdmin = userPersistence.reload("super_admin");
        clientType = clientTypeRepository.getOne(1l);
    }


    /**
     * save without required
     */
    @Test(expected = ConstraintViolationException.class)
    public void requiredFields() {
        locationPersistence.save(new Location());
    }

    /**
     * create a location
     */
    @Test
    public void create() {
        Location location = new Location();
        location.setName("Valid Name 1");
        location.setCity("Valid City 1");
        location.setPhone("630-222-8900");
        location.setState(State.IL);
        locationPersistence.save(location);
        assertThat("Location was given an id", location.getId(), is(notNullValue()));
        assertThat("system is the created by", location.getCreatedBy(), is("system"));
    }

    /**
     * save location relationship on client
     */
    @Test
    public void belongsToRelationship() {

        Client client = makeClient();
        Location location = new Location();
        location.setName("Valid Name A-Za-z-'=_;:@#&,.!() ");
        location.setCity("Valid City");
        location.setPhone("800-333-2345");
        location.setState(State.IL);
        location.setBelongsTo(client);
        location = locationPersistence.save(location);
        assertThat("Location was given an id", location.getId(), is(notNullValue()));
        client = clientPersistence.save(client);

        location = locationPersistence.reload(location);
        assertThat("Client is the same", client, is(location.getBelongsTo()));

        Page<Location> locationPage = locationPersistence.list(null, new LocationSearchFilter("Valid Name A-Za-z-'=_;"));
        assertThat("location is in the page", locationPage.getContent(), hasItem(location));

        assertThat("location has client", locationPage.getContent().iterator().next().getBelongsTo(), is(client));

    }

    /**
     * Make sure we can add locations to a client and then find them for that specific client again
     */
    @Test
    public void usingThisLocation() {
        Client client = makeClient();
        client = clientPersistence.save(client);
        Location location = new Location();
        location.setName("Covenant Hospital");
        location.setCity("Chicago");
        location.setPhone("312-555-1212");
        location.setState(State.IL);
        location.addClientUsingThisLocation(client);
        location = locationPersistence.save(location);

        Page<Location> locationPage = locationPersistence.list(null, new LocationSearchFilter(client.getId(), null, (String) null));
        assertThat("location is in the result page", locationPage.getContent(), hasItem(location));
        assertThat("client is referenced by location", locationPage.getContent().get(0).getUsingThisLocation(), hasItem(client));

    }

    /**
     * Ensure we can remove a location from a client
     */
    @Test
    public void reloadLocationForAClient() {
        Client client = makeClient();
        client = clientPersistence.save(client);
        Location location = new Location();
        location.setName("Covenant Hospital");
        location.setCity("Chicago");
        location.setPhone("312-555-1212");
        location.setState(State.IL);
        location.addClientUsingThisLocation(client);
        location = locationPersistence.save(location);

        Location reloaded = locationPersistence.reloadLocationUsedByClient(null, location.getId());
        assertThat("should not have found the location, because of no client", reloaded, is(nullValue()));

        reloaded = locationPersistence.reloadLocationUsedByClient(client, location.getId());
        assertThat("location should be found now", reloaded, is(location));
    }

    private Client makeClient() {
        Client client = new Client();
        client.setTier(ClientTier.THREE);
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setContractStart(LocalDate.now());
        client.setRegion(new ClientRegion(1l));
        client.setName("client" + System.currentTimeMillis());
        client.setType(clientType);
        client.setActive(true);
        client.setContractOwner(superAdmin);
        client.setSalesForceAccount(new SalesForce("xxxWW" + System.currentTimeMillis()));
        return clientPersistence.save(client);
    }

    /**
     * bad name
     */
    @Test(expected = ConstraintViolationException.class)
    public void invalidName() {
        Location location = new Location();
        location.setName("Invalid *");
        location.setCity("Valid City");
        location.setPhone("555-555-5555");
        location.setState(State.IL);
        locationPersistence.save(location);
    }

    /**
     * bad city
     */
    @Test(expected = ConstraintViolationException.class)
    public void invalidCity() {
        Location location = new Location();
        location.setName("Valid Name");
        location.setCity("InValid ^%");
        location.setPhone("312-222-1111");
        location.setState(State.IL);
        locationPersistence.save(location);
    }

    /**
     * Invalid phone number.. the 4th digit cannot be a 1
     */
    @Test(expected = ConstraintViolationException.class)
    public void invalidPhone() {
        Location location = new Location();
        location.setName("Valid Name");
        location.setCity("Valid");
        location.setPhone("312-122-1111");
        location.setState(State.IL);
        locationPersistence.save(location);
    }

}
