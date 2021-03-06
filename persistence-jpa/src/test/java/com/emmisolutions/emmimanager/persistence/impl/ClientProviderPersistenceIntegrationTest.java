package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.ClientProviderPersistence;
import com.emmisolutions.emmimanager.persistence.ProviderPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ProviderSpecialtyRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Test persistence layer for ClientProvider objects
 */
public class ClientProviderPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    ClientPersistence clientPersistence;

    @Resource
    ProviderPersistence providerPersistence;

    @Resource
    ClientProviderPersistence clientProviderPersistence;
    @Resource
    ProviderSpecialtyRepository providerSpecialtyRepository;
    /**
     * This test ensures that we can add providers to clients then
     * find them after saving.
     */
    @Test
    public void addProviderToClient() {
        Client client = makeClient();
        Provider provider = makeProvider();
        ClientProvider clientProvider = clientProviderPersistence.create(provider.getId(), client.getId());
        assertThat("client provider is not null", clientProvider, is(notNullValue()));
        assertThat("client provider has an id", clientProvider.getId(), is(notNullValue()));

        clientProvider = clientProviderPersistence.reload(clientProvider.getId());
        assertThat("client provider is not null", clientProvider, is(notNullValue()));

        Page<ClientProvider> clientProviderPage = clientProviderPersistence.findByClientId(client.getId(), null);
        assertThat("client provider is on the page", clientProviderPage, hasItem(clientProvider));
        
        ClientProvider cp = clientProviderPersistence.findByClientIdProviderId(client.getId(), provider.getId());
        assertThat("client provider is on the page", cp, is(notNullValue()));

        clientProviderPersistence.remove(clientProvider.getId());
        assertThat("client provider has been removed", clientProviderPersistence.reload(clientProvider.getId()), is(nullValue()));
    }

    /**
     * Reload of a null id should not error out
     */
    @Test
    public void testReloadOfNull(){
       assertThat("reload of null should yield null", clientProviderPersistence.reload(null), is(nullValue()));
    }

    /**
     * Make sure that potential providers for a client load properly
     */
    @Test
    public void findPotentialProviders(){
        // create a client
        Client client = makeClient();

        // create a bunch of providers
        Provider provider = makeProvider();
        for (int i = 0; i < 10; i++) {
             makeProvider();
        }
        // associate the Client to one of the providers
        ClientProvider clientProvider = clientProviderPersistence.create(provider.getId(), client.getId());

        // find a page of Providers using the same name that we used during create
        Page<Provider> savedProviders = providerPersistence.list(null, new ProviderSearchFilter("Client Provider Association"));
        assertThat("there should be 11 providers found", savedProviders.getTotalElements(), is(11l));

        // do the actual test
        assertThat("Load call should contain the persistent ClientProvider", clientProviderPersistence.load(client.getId(), savedProviders), hasItem(clientProvider));
    }

    /**
     * Empty load call should not error out
     */
    @Test
    public void emptyLoadCall(){
       assertThat("Empty list should come back", clientProviderPersistence.load(null, null).isEmpty(), is(true));
    }

    /**
     * Call the find method in a way that is not allowed
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void invalidFindCall(){
        clientProviderPersistence.findByClientId(null, null);
    }

    /**
     * Find by provider id
     */
    @Test
    public void findByProviderId(){
    	Client clientA = makeClient();
    	Client clientB = makeClient();

    	Provider provider = makeProvider();

    	clientProviderPersistence.create(provider.getId(), clientA.getId());
    	clientProviderPersistence.create(provider.getId(), clientB.getId());

    	Page<ClientProvider> list = clientProviderPersistence.findByProviderId(provider.getId(), null);
    	assertThat("There should be 2 clients found", list.getTotalElements(), is(2l));
    }

    /**
     * Test that save works
     */
    @Test
    public void save() {
        assertThat("client provider is saved",
                clientProviderPersistence.save(new ClientProvider(makeClient(), makeProvider())).getId(),
                is(notNullValue()));
    }

    private Client makeClient() {
        Client client = new Client();
        client.setTier(new ClientTier(3l));
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setContractStart(LocalDate.now());
        client.setRegion(new ClientRegion(1l));
        client.setName(RandomStringUtils.randomAlphanumeric(35));
        client.setType(new ClientType(1l));
        client.setActive(true);
        client.setContractOwner(new UserAdmin(1l, 0));
        client.setSalesForceAccount(new SalesForce(RandomStringUtils.randomAlphanumeric(18)));
        return clientPersistence.save(client);
    }

    private Provider makeProvider() {
        Provider provider = new Provider();
        provider.setFirstName("Client Provider Association");
        provider.setLastName(RandomStringUtils.randomAlphabetic(255));
        ProviderSpecialty specialty = new ProviderSpecialty();
        specialty.setName(RandomStringUtils.randomAlphanumeric(18));
        ProviderSpecialty savedSpecialty = providerPersistence.saveSpecialty(specialty);

        provider.setSpecialty(savedSpecialty);
        provider.setEmail("whatever@whatever.com");
        return providerPersistence.save(provider);
    }

}
