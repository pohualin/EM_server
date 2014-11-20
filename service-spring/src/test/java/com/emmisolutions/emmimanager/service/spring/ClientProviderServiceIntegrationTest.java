package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientProviderService;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.ProviderService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Integration test for the ClientProviderService API
 */
public class ClientProviderServiceIntegrationTest extends BaseIntegrationTest {

    @Resource
    ClientService clientService;

    @Resource
    ProviderService providerService;

    @Resource
    ClientProviderService clientProviderService;

    /**
     * This test is the lifecycle of a ClientProvider. It creates one, finds it, then deletes it.
     */
    @Test
    public void createFindDelete() {
        final Provider provider = makeProvider();
        Client client = makeClient();
        Set<ClientProvider> savedProviders = clientProviderService.create(client, new HashSet<Provider>() {{
            add(provider);
        }});
        assertThat("client has one provider", savedProviders.size(), is(1));
        assertThat("provider is the one we added", savedProviders.iterator().next().getProvider(), is(provider));

        ClientProvider clientProvider = clientProviderService.reload(savedProviders.iterator().next());
        assertThat("client provider was loaded", clientProvider, is(notNullValue()));

        clientProviderService.remove(clientProvider);
        assertThat("client has zero providers after delete", 0l, is(clientProviderService.findByClient(client, null).getTotalElements()));
    }

    /**
     * Ensure that finding possible providers works.. including making sure
     * existing providers function properly.
     */
    @Test
    public void findPossibleProviders(){
        // make a bunch of providers
        final Provider provider = makeProvider();
        for (int i = 0; i < 10; i++) {
            makeProvider();
        }

        // associate a client to one of those providers
        Client client = makeClient();
        Set<ClientProvider> savedProviders = clientProviderService.create(client, new HashSet<Provider>() {{
            add(provider);
        }});
        ClientProvider savedRelationship = savedProviders.iterator().next();

        // find a page of possible ClientProviders using the same name that we used during create
        Page<ClientProvider> possibleProviders =
                clientProviderService.findPossibleProvidersToAdd(client, new ProviderSearchFilter("ClientProviderServiceIntegrationTest Provider"), null);
        assertThat("there should be 11 providers found", possibleProviders.getTotalElements(), is(11l));
        assertThat("one of the ClientProvider objects should be the one we saved", possibleProviders, hasItem(savedRelationship));
    }

    /**
     * This test creates a provider on a client as well as updates it
     */
    @Test
    public void createProviderOnClient(){
        Client client = makeClient();

        Provider provider = new Provider();
        provider.setFirstName("Client Provider Association");
        provider.setLastName(RandomStringUtils.randomAlphabetic(255));
        provider.setSpecialty(new ReferenceTag(20));
        provider.setEmail("whatever@whatever.com");

        ClientProvider clientProvider = clientProviderService.create(new ClientProvider(client, provider));
        assertThat("ClientProvider is not null", clientProvider, is(notNullValue()));

        Page<ClientProvider> clientProviderPage = clientProviderService.findByClient(client, null);
        assertThat("finding client providers should include the newly created one", clientProviderPage, hasItem(clientProvider));

        // update the clientProvider
        clientProvider.setExternalId("mateo");
        provider.setActive(true);
        ClientProvider updated = clientProviderService.update(clientProvider);
        assertThat("provider was updated", updated.getProvider().isActive(), is(true));
        assertThat("client provider was updated", updated.getExternalId(), is(clientProvider.getExternalId()));
        assertThat("client provider version should be different", updated.getVersion(), is(not(clientProvider.getVersion())));
    }

    @Test
    public void findByProvider(){
    	Client clientA = makeClient();
    	Client clientB = makeClient();
    	Provider provider = makeProvider("findByProvider");
    	Set<Provider> providers = new HashSet<Provider>();
    	providers.add(provider);
    	clientProviderService.create(clientA, providers);
    	clientProviderService.create(clientB, providers);
    	Page<ClientProvider> list = clientProviderService.findByProvider(provider, null);
    	assertThat("There shoule be 2 clients found for the provider.", list.getTotalElements(), is(2l));
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void invalidCreateCall(){
        clientProviderService.create(null);
    }

    /**
     * Can't find with a null client
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void invalidFindCall(){
        clientProviderService.findByClient(null, null);
    }

    /**
     * Can't delete with a non persistent client provider
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void invalidDeleteCall(){
        clientProviderService.remove(new ClientProvider());
    }

    /**
     * Can't reload a null client provider
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void invalidReloadCall(){
        clientProviderService.reload(null);
    }

    /**
     * Can't find possible providers for a null client
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void invalidFindPossibleCall(){
        clientProviderService.findPossibleProvidersToAdd(null, null, null);
    }

    /**
     * Test invalid api access for create
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void invalidCreate(){
         clientProviderService.create(null, null);
    }


    private Client makeClient() {
        Client client = new Client();
        client.setTier(new ClientTier(3l));
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setContractStart(LocalDate.now());
        client.setRegion(new ClientRegion(1l));
        client.setName(RandomStringUtils.randomAlphabetic(255));
        client.setType(new ClientType(1l));
        client.setActive(true);
        client.setContractOwner(new UserAdmin(1l, 0));
        client.setSalesForceAccount(new SalesForce(RandomStringUtils.randomAlphanumeric(18)));
        return clientService.create(client);
    }

    private Provider makeProvider(String firstName) {
        Provider provider = new Provider();
        if(StringUtils.isBlank(firstName)){
        	firstName = "ClientProviderServiceIntegrationTest Provider";
        }
        provider.setFirstName(firstName);
        provider.setLastName(RandomStringUtils.randomAlphabetic(255));
        provider.setSpecialty(new ReferenceTag(20));
        provider.setEmail("whatever@whatever.com");
        return providerService.create(provider);
    }

    private Provider makeProvider(){
    	return makeProvider("");
    }
}
