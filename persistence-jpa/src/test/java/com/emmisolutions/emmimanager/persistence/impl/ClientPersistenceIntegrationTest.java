package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ClientRepository;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

import static com.emmisolutions.emmimanager.model.ClientSearchFilter.StatusFilter.ACTIVE_ONLY;
import static com.emmisolutions.emmimanager.model.ClientSearchFilter.StatusFilter.INACTIVE_ONLY;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * The Client Persistence test
 */
public class ClientPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    ClientPersistence clientPersistence;

    @Resource
    UserPersistence userPersistence;

    @Resource
    ClientRepository clientRepository;

    private User superAdmin;

    /**
     * Before each test
     */
    @Before
    public void init() {
        superAdmin = userPersistence.reload("super_admin");
    }

    /**
     * Save success
     */
    @Test
    public void save() {
        Client client = new Client();
        client.setTier(ClientTier.THREE);
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setContractStart(LocalDate.now());
        client.setRegion(ClientRegion.NORTHEAST);
        client.setName("Test Client");
        client.setType(ClientType.PROVIDER);
        client.setActive(false);
        client.setContractOwner(superAdmin);
        client.setSalesForceAccount(new SalesForce("xxxWW" + System.currentTimeMillis()));
        client = clientPersistence.save(client);
        assertThat("Client was given an id", client.getId(), is(notNullValue()));
        assertThat("system is the created by", client.getCreatedBy(), is("system"));
    }

    /**
     * List test
     */
    @Test
    public void list() {
        // push a bunch of clients to the db
        for (int i = 0; i < 200; i++) {
            clientRepository.save(makeClient(i));
        }

        Page<Client> clientPage = clientPersistence.list(null, null);
        assertThat("found all of the clients", clientPage.getTotalElements(), is(200l));
        assertThat("there were 4 pages", clientPage.getTotalPages(), is(4));
        assertThat("there are 50 items in the page", clientPage.getSize(), is(50));
        assertThat("we are on page 0", clientPage.getNumber(), is(0));

        // test list with filters
        String[] nameFilters = new String[]{"HoSpItAl"};
        clientPage = clientPersistence.list(new PageRequest(2, 10), new ClientSearchFilter(nameFilters));
        assertThat("found all of the clients", clientPage.getTotalElements(), is(200l));
        assertThat("there were 20 pages", clientPage.getTotalPages(), is(20));
        assertThat("there are 10 items in the page", clientPage.getSize(), is(10));
        assertThat("we are on page 2", clientPage.getNumber(), is(2));

        // search for inactive only, page size 20
        clientPage = clientPersistence.list(new PageRequest(3, 20), new ClientSearchFilter(INACTIVE_ONLY, nameFilters));
        assertThat("found half of the clients", clientPage.getTotalElements(), is(100l));
        assertThat("there were 5 pages", clientPage.getTotalPages(), is(5));
        assertThat("there are 20 items in the page", clientPage.getSize(), is(20));
        assertThat("we are on page 3", clientPage.getNumber(), is(3));

        // search for active, page size 100, multiple names
        clientPage = clientPersistence.list(new PageRequest(0, 100), new ClientSearchFilter(ACTIVE_ONLY, "client 5", "client 9"));
        assertThat("only clients starting with 5 or 9 should come back", clientPage.getTotalElements(), is(10l));
        assertThat("there is 1 page", clientPage.getTotalPages(), is(1));
        assertThat("there is nothing on this page", clientPage.getNumberOfElements(), is(10));
        assertThat("there are 100 items in the page", clientPage.getSize(), is(100));
        assertThat("we are on page 0", clientPage.getNumber(), is(0));

        // request a page out of bounds
        clientPage = clientPersistence.list(new PageRequest(10, 100), new ClientSearchFilter(ACTIVE_ONLY, "client 5", "client 9"));
        assertThat("only clients starting with 5 or 9 should come back", clientPage.getTotalElements(), is(10l));
        assertThat("there is 1 page", clientPage.getTotalPages(), is(1));
        assertThat("there is nothing on this page", clientPage.getNumberOfElements(), is(0));
        assertThat("there are 100 items in the page", clientPage.getSize(), is(100));
        assertThat("we are on page 10", clientPage.getNumber(), is(10));
    }

    /**
     * Location test
     */
    @Test
    public void addLocation() {
        Client client = clientPersistence.save(makeClient(201));
        Location location = new Location();
        location.setName("Valid Name");
        location.setCity("Valid City");
        location.setPhone("phone number");
        location.setState(State.IL);
        client.getLocations().add(location);
        client = clientPersistence.save(client);


        Page<Client> clientPage = clientPersistence.list(null, new ClientSearchFilter("Demo hospital client 201"));
        assertThat("Client should be found", clientPage.getContent(), hasItem(client));
        assertThat("Location should be attached to the client",
                clientPage.getContent().get(0).getLocations().iterator().next().getName(), is("Valid Name"));
    }

    private Client makeClient(long i) {
        Client client = new Client();
        client.setActive(i % 2 == 0);
        client.setName("Demo hospital client " + i);
        client.setType(ClientType.PROVIDER);
        client.setRegion(ClientRegion.NORTHEAST);
        client.setTier(ClientTier.THREE);
        client.setContractOwner(superAdmin);
        client.setContractStart(LocalDate.now());
        client.setContractEnd(LocalDate.now().plusYears(2));
        client.setSalesForceAccount(new SalesForce("xxxWW" + System.currentTimeMillis()));
        return client;
    }
}
