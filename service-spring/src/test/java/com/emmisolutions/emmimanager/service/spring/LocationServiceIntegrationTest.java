package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.persistence.LocationPersistence;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.LocationService;
import com.emmisolutions.emmimanager.service.UserService;
import org.hamcrest.CustomMatcher;
import org.joda.time.LocalDate;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocationServiceIntegrationTest extends BaseIntegrationTest {

    @Resource
    LocationService locationService;

    @Resource
    LocationPersistence locationPersistence;

    @Resource
    ClientService clientService;

    @Resource
    UserService userService;

    @Test
    public void aList() {
        for (int i = 0; i < 200; i++) {
            locationPersistence.save(makeLocation(i));
        }
        Page<Location> locationPage = locationService.list(null, null);
        assertThat("there are 200 items", locationPage.getTotalElements(), is(200l));

        locationPage = locationService.list(new PageRequest(1, 10),
                new LocationSearchFilter(LocationSearchFilter.StatusFilter.ACTIVE_ONLY, "valid", "name"));
        assertThat("there are 100 items", locationPage.getTotalElements(), is(100l));
    }

    @Test
    public void createLocationViaClient() {
        // create a client with a location
        Client client = new Client();
        client.setType(ClientType.PROVIDER);
        client.setContractStart(LocalDate.now());
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setName("whatever");
        client.setContractOwner(userService.save(new User("username", "pw")));
        client.setSalesForceAccount(new SalesForce("account num"));
        client.getLocations().add(makeLocation(0));

        // this should transitively persist the location
        clientService.create(client);

        // find the location that was just inserted
        Page<Location> locations = locationService.list(new LocationSearchFilter("valid name a"));
        assertThat("location should have been saved when the client was persisted",
                locations.getContent(),
                hasItem(new CustomMatcher<Location>("name") {
                    @Override
                    public boolean matches(Object item) {
                        return ((Location) item).getName().equalsIgnoreCase("valid name a");
                    }
                }));
    }

    private Location makeLocation(long i) {
        Location location = new Location();
        String suffix = encode(i);
        location.setName("Valid Name " + suffix);
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
