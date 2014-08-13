package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.LocationPersistence;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class LocationPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    LocationPersistence locationPersistence;

    @Resource
    ClientPersistence clientPersistence;

    @Resource
    UserPersistence userPersistence;

    private User superAdmin;

    @Before
    public void init(){
        superAdmin = userPersistence.reload("super_admin");
    }


    @Test(expected = ConstraintViolationException.class)
    public void requiredFields() {
        locationPersistence.save(new Location());
    }

    @Test
    public void create() {
        Location location = new Location();
        location.setName("Valid Name");
        location.setCity("Valid City");
        location.setPhone("phone number");
        location.setState(State.IL);
        locationPersistence.save(location);
        assertThat("Location was given an id", location.getId(), is(notNullValue()));
        assertThat("system is the created by", location.getCreatedBy(), is("system"));
    }

    @Test
    public void belongsToRelationship(){
        Client client = new Client();
        client.setTier(ClientTier.THREE);
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setContractStart(LocalDate.now());
        client.setRegion(ClientRegion.NORTHEAST);
        client.setName("Test Client");
        client.setType(ClientType.PROVIDER);
        client.setActive(true);
        client.setContractOwner(superAdmin);
        client.setSalesForceAccount(new SalesForce("xxxWW" + System.currentTimeMillis()));
        client = clientPersistence.save(client);

        Location location = new Location();
        location.setName("Valid Name A-Za-z-'=_;:@#&,.!() ");
        location.setCity("Valid City");
        location.setPhone("phone number");
        location.setState(State.IL);
        location.setBelongsTo(client);
        location = locationPersistence.save(location);
        assertThat("Location was given an id", location.getId(), is(notNullValue()));

        location = locationPersistence.reload(location);
        assertThat("Client is the same", client, is(location.getBelongsTo()));

        Page<Location> locationPage = locationPersistence.list(null, new LocationSearchFilter("Valid Name A-Za-z-'=_;"));
        assertThat("location is in the page", locationPage.getContent(), hasItem(location));

        assertThat("location has client", locationPage.getContent().iterator().next().getBelongsTo(), is(client));
    }

    @Test(expected = ConstraintViolationException.class)
    public void invalidName(){
        Location location = new Location();
        location.setName("Invalid *");
        location.setCity("Valid City");
        location.setPhone("phone number");
        location.setState(State.IL);
        locationPersistence.save(location);
    }

    @Test(expected = ConstraintViolationException.class)
    public void invalidCity(){
        Location location = new Location();
        location.setName("Valid Name");
        location.setCity("InValid ^%");
        location.setPhone("phone number");
        location.setState(State.IL);
        locationPersistence.save(location);
    }

}
