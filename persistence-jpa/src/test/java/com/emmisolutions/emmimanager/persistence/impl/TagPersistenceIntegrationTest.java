package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientRegion;
import com.emmisolutions.emmimanager.model.ClientTier;
import com.emmisolutions.emmimanager.model.ClientType;
import com.emmisolutions.emmimanager.model.Group;
import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.model.TagSearchFilter;
import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.GroupPersistence;
import com.emmisolutions.emmimanager.persistence.TagPersistence;
import com.emmisolutions.emmimanager.persistence.UserPersistence;

public class TagPersistenceIntegrationTest extends BaseIntegrationTest {

	@Resource
	TagPersistence tagPersistence;

    @Resource
    ClientPersistence clientPersistence;
    
    @Resource
    GroupPersistence groupPersistence;
    
	@Resource
	UserPersistence userPersistence;
	
	private User superAdmin;

	@Before
	public void init() {
	    superAdmin = userPersistence.reload("super_admin");
	}
	
	@Test
	public void saveTag() {
		Group group = createGroup();
		Tag tagOne = new Tag();
		tagOne.setName("Tag One");
		tagOne.setGroup(group);
		Tag tagSaved = tagPersistence.save(tagOne);
		assertThat("TagOne persisted:", tagSaved.getId(), is(notNullValue()));
	}
	
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
	    client.setType(ClientType.PROVIDER);
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
	
	private List<Tag> listOfTags(){
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
		return tags;
	}
}
