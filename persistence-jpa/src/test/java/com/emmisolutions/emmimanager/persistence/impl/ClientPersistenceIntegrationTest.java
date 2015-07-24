package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.UserAdminPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ClientTypeRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.LocalDate;
import org.joda.time.Minutes;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

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
    UserAdminPersistence userAdminPersistence;

    UserAdmin superAdmin;

    @Resource
    ClientTypeRepository clientTypeRepository;

    ClientType clientType;

    @Before
    public void init() {
        superAdmin = userAdminPersistence.reload("super_admin");
        clientType = clientTypeRepository.getOne(1l);
    }

    /**
     * Save success
     */
    @Test
    public void save() {
        Client client = new Client();
        client.setTier(new ClientTier(3l));
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setContractStart(LocalDate.now());
        client.setRegion(new ClientRegion(1l));
        client.setName("Test Client");
        client.setType(clientType);
        client.setActive(false);
        client.setContractOwner(superAdmin);
        client.setSalesForceAccount(new SalesForce(RandomStringUtils.randomAlphanumeric(18)));
        client = clientPersistence.save(client);
        assertThat("Client was given an id", client.getId(), is(notNullValue()));
    }

    /**
     * Prohibited characters in name should fail
     */
    @Test(expected = ConstraintViolationException.class)
    public void em_68_BadChars() {
        Client client = new Client();
        client.setTier(new ClientTier(3l));
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setContractStart(LocalDate.now());
        client.setRegion(new ClientRegion(1l));
        client.setName("$ % ^ * \\");
        client.setType(clientType);
        client.setActive(false);
        client.setContractOwner(superAdmin);
        client.setSalesForceAccount(new SalesForce(RandomStringUtils.randomAlphanumeric(18)));
        clientPersistence.save(client);
    }

    /**
     * All valid characters should be allowed
     */
    @Test
    public void em_68_AllValidChars() {
        Client client = new Client();
        client.setTier(new ClientTier(3l));
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setContractStart(LocalDate.now());
        client.setRegion(new ClientRegion(1l));
        client.setName("Aa1= ' _ ; : ` @ # & , . ! ( ) /");
        client.setType(clientType);
        client.setActive(false);
        client.setContractOwner(superAdmin);
        client.setSalesForceAccount(new SalesForce(RandomStringUtils.randomAlphanumeric(18)));
        clientPersistence.save(client);
        assertThat("Client was given an id", client.getId(), is(notNullValue()));
    }

    /**
     * List test
     */
    @Test
    public void list() {
        // push a bunch of clients to the db
        for (int i = 0; i < 200; i++) {
        	clientPersistence.save(makeClient(i));
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
        assertThat("only clients containing with 5 or 9 should come back", clientPage.getTotalElements(), is(20l));
        assertThat("there is 1 page", clientPage.getTotalPages(), is(1));
        assertThat("there are 20 on the page", clientPage.getNumberOfElements(), is(20));
        assertThat("there are 100 items in the page", clientPage.getSize(), is(100));
        assertThat("we are on page 0", clientPage.getNumber(), is(0));

        // request a page out of bounds
        clientPage = clientPersistence.list(new PageRequest(10, 100), new ClientSearchFilter(ACTIVE_ONLY, "client 5", "client 9"));
        assertThat("only clients starting with 5 or 9 should come back", clientPage.getTotalElements(), is(20l));
        assertThat("there is 1 page", clientPage.getTotalPages(), is(1));
        assertThat("there is nothing on this page", clientPage.getNumberOfElements(), is(0));
        assertThat("there are 100 items in the page", clientPage.getSize(), is(100));
        assertThat("we are on page 10", clientPage.getNumber(), is(10));
    }

    /**
     * search by normalized name test
     */
    @Test
    public void search() {

	   Client client = new Client();
	   client.setActive(true);
	   client.setName("Demo hospital client 1" );
	   client.setType(clientType);
	   client.setRegion(new ClientRegion(1l));
	   client.setTier(new ClientTier(3l));
	   client.setContractOwner(superAdmin);
	   client.setContractStart(LocalDate.now());
	   client.setContractEnd(LocalDate.now().plusYears(2));
	   client.setSalesForceAccount(new SalesForce(RandomStringUtils.randomAlphanumeric(18)));
       clientPersistence.save(client);

    	client = clientPersistence.findByNormalizedName("demo hospital client 1");
        assertThat("Client exists", client.getName(), is("Demo hospital client 1"));
        assertThat("Client exists", client.getNormalizedName(), is("demohospitalclient1"));

        assertThat("Client do not exists", clientPersistence.findByNormalizedName("demo hospital cloient"), is(nullValue()));

        assertThat("Client do not exists", clientPersistence.findByNormalizedName(null), is(nullValue()));

    }

    /**
     * Out of order search terms should still find the client, also
     * tests case insensitive as well as special character stripping.
     */
    @Test
    public void testSearchOutOfOrder(){
        Client client = new Client();
        client.setActive(true);
        client.setName("Dr. Quinn, Medicine Woman" );
        client.setType(clientType);
        client.setRegion(new ClientRegion(1l));
        client.setTier(new ClientTier(3l));
        client.setContractOwner(superAdmin);
        client.setContractStart(LocalDate.now());
        client.setContractEnd(LocalDate.now().plusYears(2));
        client.setSalesForceAccount(new SalesForce(RandomStringUtils.randomAlphanumeric(18)));
        clientPersistence.save(client);

        Page<Client> clientPage = clientPersistence.list(null, new ClientSearchFilter("woman quinn dr"));
        assertThat("should have found the good doctor", clientPage.getContent(), hasItem(client));

        clientPage = clientPersistence.list(null, new ClientSearchFilter("woman! woman woman woman qui,nn woman woman woman dr%$%$%"));
        assertThat("still should have found the good doctor b/c of stripping and dupe cleaning", clientPage.getContent(), hasItem(client));
    }

    /**
     * Ensure that special characters are stripped out of normalized name searches
     */
    @Test
    public void searchSpecialCharacters() {

	   Client client = new Client();
	   client.setActive(true);
	   client.setName("Demo-hospital-'=_;:`@#&,.!()client 1" );
	   client.setType(clientType);
	   client.setRegion(new ClientRegion(1l));
	   client.setTier(new ClientTier(3l));
	   client.setContractOwner(superAdmin);
	   client.setContractStart(LocalDate.now());
	   client.setContractEnd(LocalDate.now().plusYears(2));
	   client.setSalesForceAccount(new SalesForce(RandomStringUtils.randomAlphanumeric(18)));
       clientPersistence.save(client);

    	client = clientPersistence.findByNormalizedName("Demo-hospital-'=_;:`@#&,.!()client 1");
        assertThat("Client exists", client.getName(), is("Demo-hospital-'=_;:`@#&,.!()client 1"));
        assertThat("Client exists", client.getNormalizedName(), is("demohospitalclient1"));

 	   client = new Client();
 	   client.setActive(true);
 	   client.setName("Demo hospital '=_;:`@#&,.!()client 2" );
 	   client.setType(clientType);
 	   client.setRegion(new ClientRegion(1l));
 	   client.setTier(new ClientTier(3l));
 	   client.setContractOwner(superAdmin);
 	   client.setContractStart(LocalDate.now());
 	   client.setContractEnd(LocalDate.now().plusYears(2));
 	   client.setSalesForceAccount(new SalesForce(RandomStringUtils.randomAlphanumeric(18)));
        clientPersistence.save(client);

     	client = clientPersistence.findByNormalizedName("Demo hospital '=_;:`@#&,.!()client 2");
         assertThat("Client exists", client.getName(), is("Demo hospital '=_;:`@#&,.!()client 2"));
         assertThat("Client exists", client.getNormalizedName(), is("demohospitalclient2"));

   	   client = new Client();
   	   client.setActive(true);
   	   client.setName("test client123" );
   	   client.setType(clientType);
   	   client.setRegion(new ClientRegion(1l));
   	   client.setTier(new ClientTier(3l));
   	   client.setContractOwner(superAdmin);
   	   client.setContractStart(LocalDate.now());
   	   client.setContractEnd(LocalDate.now().plusYears(2));
   	   client.setSalesForceAccount(new SalesForce(RandomStringUtils.randomAlphanumeric(18)));
          clientPersistence.save(client);

       	client = clientPersistence.findByNormalizedName("test-client123");
        assertThat("Client exists", client.getName(), is("test client123"));
        assertThat("Client exists", client.getNormalizedName(), is("testclient123"));
    }

    /**
     * Test when the contract end date is after the contract start date
     */
    @Test(expected = ConstraintViolationException.class)
    public void startAfterEnd() {
        Client client = makeClient(1);
        client.setContractStart(LocalDate.now());
        client.setContractEnd(LocalDate.now().minusDays(2));
        clientPersistence.save(client);
    }

    /**
     * Test when the contract end date is not a full day ahead of
     * the contract start date
     */
    @Test(expected = ConstraintViolationException.class)
    public void lessThanOneDayAhead() {
        Client client = makeClient(1);
        client.setContractStart(LocalDate.now());
        client.setContractEnd(LocalDate.now().plus(Minutes.minutes(12)));
        clientPersistence.save(client);
    }

    /**
     * Reference data works
     */
    @Test
    public void refData() {
        assertThat("types not empty", clientPersistence.getAllClientTypes().isEmpty(), is(false));
        assertThat("regions not empty", clientPersistence.getAllRegionTypes().isEmpty(), is(false));
        assertThat("tiers not empty", clientPersistence.getAllClientTiers().isEmpty(), is(false));
    }

    /**
     * Test when the contract end date is exactly one day ahead
     * of the contract start date
     */
    @Test
    public void exactlyOneDayAhead() {
        Client client = makeClient(1);
        client.setContractStart(LocalDate.now());
        client.setContractEnd(LocalDate.now().plusDays(1));
        clientPersistence.save(client);
    }

    private Client makeClient(long i) {
        Client client = new Client();
        client.setActive(i % 2 == 0);
        client.setName("Demo hospital client " + i);
        client.setType(clientType);
        client.setRegion(new ClientRegion(1l));
        client.setTier(new ClientTier(3l));
        client.setContractOwner(superAdmin);
        client.setContractStart(LocalDate.now());
        client.setContractEnd(LocalDate.now().plusYears(2));
        client.setSalesForceAccount(new SalesForce(RandomStringUtils.randomAlphanumeric(18)));
        return client;
    }
}
