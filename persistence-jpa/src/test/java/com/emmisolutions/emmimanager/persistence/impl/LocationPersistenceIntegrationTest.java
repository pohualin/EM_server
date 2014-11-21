package com.emmisolutions.emmimanager.persistence.impl;


import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientLocation;
import com.emmisolutions.emmimanager.model.ClientRegion;
import com.emmisolutions.emmimanager.model.ClientTier;
import com.emmisolutions.emmimanager.model.ClientType;
import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.LocationSearchFilter;
import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.model.State;
import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientLocationPersistence;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.LocationPersistence;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ClientTypeRepository;

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

    @Resource
    ClientLocationPersistence clientLocationPersistence;

    UserAdmin superAdmin;

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
     * Make sure locations belonging to a client can be searched for
     */
    @Test
    public void belongsToFilter() {
        Client client = makeClient();
        client = clientPersistence.save(client);
        Location location = new Location();
        location.setName("Covenant Hospital");
        location.setCity("Chicago");
        location.setPhone("312-555-1212");
        location.setState(State.IL);
        location.setBelongsTo(client);
        location = locationPersistence.save(location);

        Page<Location> locationPage = locationPersistence.list(null, new LocationSearchFilter(client.getId(), null, (String) null));
        assertThat("location is in the result page", locationPage.getContent(), hasItem(location));

    }

    @Test
    public void excludeClient() {
        Client client = makeClient();
        client = clientPersistence.save(client);
        Location location = new Location();
        location.setName("Covenant Hospital");
        location.setCity("Chicago");
        location.setPhone("312-555-1212");
        location.setState(State.IL);
        location.setBelongsTo(client);
        location = locationPersistence.save(location);
        
        Client client2 = makeClient();
        client2 = clientPersistence.save(client2);
        Location location2 = new Location();
        location2.setName("Covenant Hospital");
        location2.setCity("Chicago");
        location2.setPhone("312-555-1212");
        location2.setState(State.IL);
        location2.setBelongsTo(client2);
        location2 = locationPersistence.save(location2);

        ClientLocation clientLocation = clientLocationPersistence.create(location.getId(), client.getId());
        //ClientLocation clientLocation2 = clientLocationPersistence.create(location2.getId(), client2.getId());
        
        Page<Location> locationPage = locationPersistence.listWithoutLocations(null, new LocationSearchFilter(client.getId(), null, (String) null));
        assertThat("location2 is in the result page", locationPage.getContent(), hasItem(location2));

    }

    private Client makeClient() {
        Client client = new Client();
        client.setTier(new ClientTier(3l));
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

    @Test
    public void reloadNull(){
        assertThat("reloading null location should result in null", locationPersistence.reload(null), is(nullValue()));
        assertThat("reloading location without id should result in null", locationPersistence.reload(new Location()), is(nullValue()));
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
