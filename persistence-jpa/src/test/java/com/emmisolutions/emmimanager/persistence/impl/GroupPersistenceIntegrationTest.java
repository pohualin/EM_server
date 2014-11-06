package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.GroupPersistence;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ClientRepository;
import com.emmisolutions.emmimanager.persistence.repo.ClientTypeRepository;
import com.emmisolutions.emmimanager.persistence.repo.GroupRepository;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;

import javax.annotation.Resource;
import java.util.HashSet;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;


/**
 * Group Persistence Integration Test
 *
 */
public class GroupPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    ClientPersistence clientPersistence;
    
    @Resource
    GroupPersistence groupPersistence;
    
	@Resource
	UserPersistence userPersistence;
	
	@Resource
	GroupRepository groupRepository;
	
	@Resource
	ClientRepository clientRepository;

    @Resource
    ClientTypeRepository clientTypeRepository;

	User superAdmin;

    ClientType clientType;

	@Before
	public void init() {
	    superAdmin = userPersistence.reload("super_admin");
        clientType = clientTypeRepository.getOne(1l);
	}
	
	/**
	 * 	Big method.. save some groups, search for em, remove groups,
	 */
	@Test
	public void listGroupsByClientID(){

        // create groups
        Group groupOne = new Group();
		groupOne.setName("TestGroup1");
		Client clientOne = makeClient();
		clientPersistence.save(clientOne);
		groupOne.setClient(clientPersistence.reload(clientOne.getId()));
		groupOne = groupPersistence.save(groupOne);
		Group two = new Group();
        two.setName("TestGroup2");
        two.setClient(clientPersistence.reload(clientOne.getId()));
		final Group groupTwo = groupPersistence.reload(groupPersistence.save(two).getId());

        // search for groups that were just saved
		GroupSearchFilter gsf = new GroupSearchFilter(clientOne.getId());
		Page<Group> groupPage = groupPersistence.list(null, gsf);
		assertThat("found all of the clients", groupPage.getTotalElements(), is(2l));
		assertThat("we are on page 0", groupPage.getNumber(), is(0));
        assertThat("the two groups come back", groupPage, hasItems(groupOne, groupTwo));
		assertThat("we are on page 0", groupPage.getContent().iterator().next().getClient().getId(), is(clientOne.getId()));

        // remove groups
        assertThat("remove one of the groups", groupPersistence.removeGroupsThatAreNotAssociatedWith(clientOne.getId(), new HashSet<Long>(){{
            add(groupTwo.getId());
        }}), is(1l));
        groupPage = groupPersistence.list(null, gsf);
        assertThat("found a single group", groupPage.getTotalElements(), is(1l));
        assertThat("group two should be the only one left", groupPage, hasItem(groupTwo));

        // remove for the client
        assertThat("remove last remaining group at the client level", groupPersistence.removeGroupsThatAreNotAssociatedWith(clientOne.getId(), null), is(1l));
        groupPage = groupPersistence.list(null, gsf);
        assertThat("found a single group", groupPage.getTotalElements(), is(0l));
	}

    /**
     * Reloading a null should return a null
     */
    @Test
    public void reloadNull(){
        assertThat("reload of null returns null", groupPersistence.reload(null), is(nullValue()));
    }

    @Test
    public void removeGroups(){
        assertThat("remove of null should return zero", groupPersistence.removeGroupsThatAreNotAssociatedWith(null, null), is(0l));
    }

    private Client makeClient(){
        Client client = new Client();
        client.setTier(new ClientTier(3l));
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setContractStart(LocalDate.now());
        client.setRegion(new ClientRegion(1l));
        client.setName("Test Client");
        client.setType(clientType);
        client.setActive(false);
        client.setContractOwner(superAdmin);
        client.setSalesForceAccount(new SalesForce("xxxWW" + System.currentTimeMillis()));
        return client;
    }
}