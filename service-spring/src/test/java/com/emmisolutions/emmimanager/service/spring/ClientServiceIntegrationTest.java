package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientSearchFilter;
import com.emmisolutions.emmimanager.model.ClientType;
import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.persistence.UserAdminPersistence;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.UserAdminService;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.data.domain.Page;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import static com.emmisolutions.emmimanager.model.ClientSearchFilter.StatusFilter.INACTIVE_ONLY;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Client service integration
 */
public class ClientServiceIntegrationTest extends BaseIntegrationTest {

    @Resource
    ClientService clientService;

    @Resource
    UserAdminService userAdminService;

    @Resource
    UserAdminPersistence userAdminPersistence;

    /**
     * Not all required fields
     */
    @Test(expected = ConstraintViolationException.class)
    public void createNotAllRequired() {
        Client client = new Client();
        clientService.create(client);
    }

    /**
     * bad create
     */
    @Test(expected = IllegalArgumentException.class)
    public void badCreate() {
        clientService.create(null);
    }

    /**
     * bad update
     */
    @Test(expected = IllegalArgumentException.class)
    public void badUpdate() {
        Client toUpdate = new Client();
        toUpdate.setId(1l);
        clientService.update(toUpdate);
    }

    /**
     * Create successfully then find.
     */
    @Test
    public void create() {
        Client client = clientService.create(makeClient("toCreate"));
        assertThat("client was created successfully", client.getId(), is(notNullValue()));

        assertThat("can find the client", clientService.list(new ClientSearchFilter("toCreate")),
            hasItem(client));

        assertThat("can find the client by normalized name", clientService.findByNormalizedName("toCreate"),
            is(client));
    }

    /**
     * update successfully
     */
    @Test
    public void update() {
        final String clientName = "forUpdating";
        clientService.create(makeClient(clientName));
        Page<Client> page = clientService.list(new ClientSearchFilter(INACTIVE_ONLY, clientName));
        Client toUpdate = page.getContent().iterator().next();
        Integer version = toUpdate.getVersion();
        toUpdate.setName("mateo wuz here");
        toUpdate = clientService.update(toUpdate);
        page = clientService.list(new ClientSearchFilter(INACTIVE_ONLY, clientName));
        assertThat("shouldn't find it anymore", page.getTotalElements(), is(0l));
        assertThat("version should have incremented on update", version ,is(toUpdate.getVersion() - 1));
    }

    /**
     * Reference data works
     */
    @Test
    public void refData(){
        assertThat("types not empty", clientService.getAllClientTypes().isEmpty(), is(false));
        assertThat("regions not empty", clientService.getAllClientRegions().isEmpty(), is(false));
        assertThat("tiers not empty", clientService.getAllClientTiers().isEmpty(), is(false));
    }


    /**
     * Fetch contract owners
     */
    @Test
    public void contractUserFetch(){
        UserAdmin contractOwner = userAdminPersistence.reload("contract_owner");
        Page<UserAdmin> ret = clientService.listPotentialContractOwners(null);
        assertThat("Users should be returned", ret.hasContent(), is(true));
        assertThat("contract_owner should be in the page", ret.getContent(), hasItem(contractOwner));
    }

    private Client makeClient(String clientName){
        Client client = new Client();
        client.setType(new ClientType(1l));
        client.setContractStart(LocalDate.now());
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setName(clientName);
        client.setContractOwner(new UserAdmin(1l, 0));
        client.setSalesForceAccount(new SalesForce(RandomStringUtils.randomAlphanumeric(18)));
        return client;
    }

}
