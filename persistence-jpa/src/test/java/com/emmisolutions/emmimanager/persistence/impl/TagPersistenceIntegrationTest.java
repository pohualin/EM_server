package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.persistence.*;
import com.emmisolutions.emmimanager.persistence.repo.ClientTypeRepository;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


/**
 * Tag persistence integration test
 *
 */
public class TagPersistenceIntegrationTest extends BaseIntegrationTest {

	@Resource
	TagPersistence tagPersistence;

    @Resource
    ClientPersistence clientPersistence;
    
    @Resource
    GroupPersistence groupPersistence;
    
	@Resource
	UserPersistence userPersistence;
	
	User superAdmin;

    @Resource
    ClientTypeRepository clientTypeRepository;

    ClientType clientType;

    @Before
    public void init() {
        superAdmin = userPersistence.reload("super_admin");
        clientType = clientTypeRepository.getOne(1l);
    }
	
	/**
	 * 	Test list of tags by group id
	 */
	@Test
	public void testListTagsByGroupId(){
		Group group = createGroup();
		group.setName("TestGroup");
		
		Tag tagOne = new Tag();
		Tag tagTwo = new Tag();
		tagOne.setName("TagOne");
		tagTwo.setName("TagTwo");;
		tagOne.setGroup(group);
		tagTwo.setGroup(group);
		
		List<Tag> tags = new ArrayList<Tag>();
		
		tags.add(tagOne);
		tags.add(tagTwo);
		tagPersistence.createAll(tags);
		
		TagSearchFilter searchFilter = new TagSearchFilter(group.getId());
		Page<Tag> retreivedTags = tagPersistence.listTagsByGroupId(null, searchFilter);
		
		assertThat("List tags by Group ID retrieved two tags", retreivedTags.getTotalElements(), is(2l));
        assertThat("there is 1 pages", retreivedTags.getTotalPages(), is(1));
        assertThat("we are on page 0", retreivedTags.getNumber(), is(0));
        assertThat("Tag Inserted is equal to tag received", retreivedTags.getContent().get(0).getName(), is("TagOne"));
        assertThat("Tag belongs to the group", retreivedTags.getContent().iterator().next().getGroup().getId(), is(group.getId()));
	}
	
	private Group createGroup(){
	    Client client = new Client();
	    client.setTier(ClientTier.THREE);
	    client.setContractEnd(LocalDate.now().plusYears(1));
	    client.setContractStart(LocalDate.now());
	    client.setRegion(ClientRegion.NORTHEAST);
	    client.setName("Test Client");
	    client.setType(clientType);
	    client.setActive(false);
	    client.setContractOwner(superAdmin);
        client.setSalesForceAccount(new SalesForce("xxxWW" + System.currentTimeMillis()));
        clientPersistence.save(client);

		Group group = new Group();
		group.setName("Test Group");
		group.setClient(clientPersistence.reload(client.getId()));
		group = groupPersistence.save(group);
		return group;
	}
}
