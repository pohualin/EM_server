package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientService;
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
    UserPersistence userPersistence;

    /**
     * Not all required fields
     */
    @Test(expected = ConstraintViolationException.class)
    public void createNotAllRequired() {
        Client client = new Client();
        clientService.create(client);
    }

    /**
     * Create successfully
     */
    @Test
    public void create() {
        Client client = clientService.create(makeClient("toCreate", "me"));
        assertThat("client was created successfully", client.getId(), is(notNullValue()));
    }

    /**
     * update successfully
     */
    @Test
    public void update() {
        final String clientName = "forUpdating";
        clientService.create(makeClient(clientName, "update_user"));
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
     * Fetch contract owners
     */
    @Test
    public void contractUserFetch(){
        User contractOwner = userPersistence.reload("contract_owner");
        Page<User> ret = clientService.listPotentialContractOwners(null);
        assertThat("Users should be returned", ret.hasContent(), is(true));
        assertThat("contract_owner should be in the page", ret.getContent(), hasItem(contractOwner));
    }

}
