package com.emmisolutions.emmimanager.service.spring;

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
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.GroupService;
import com.emmisolutions.emmimanager.service.TagService;

public class TagServiceIntegrationTest extends BaseIntegrationTest {

	@Resource
	GroupService groupService;
	
	@Resource
	TagService tagService;

	@Resource
	UserPersistence userPersistence;

	@Resource
	ClientService clientService;

	private User superAdmin;

	@Before
	public void init() {
		superAdmin = userPersistence.reload("super_admin");
	}

	private Group createGroup() {
		Client client = new Client();
		client.setTier(ClientTier.THREE);
		client.setContractEnd(LocalDate.now().plusYears(1));
		client.setContractStart(LocalDate.now());
		client.setRegion(ClientRegion.NORTHEAST);
		client.setName("Test Client");
		client.setType(ClientType.PROVIDER);
		client.setActive(false);
		client.setContractOwner(superAdmin);
		client.setSalesForceAccount(new SalesForce("xxxWW"
				+ System.currentTimeMillis()));
		clientService.create(client);

		Group group = new Group();
		group.setName("Test Group");
		group.setClient(clientService.reload(client));
		group = groupService.save(group);
		return group;
	}
	
	@Test
	public void saveTag() {
		Group group = createGroup();
		Tag tagOne = new Tag();
		tagOne.setName("Tag One");
		tagOne.setGroup(group);
		Tag tagSaved = tagService.save(tagOne);
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
		tagService.saveAll(tags);
		
		TagSearchFilter searchFilter = new TagSearchFilter(group.getId());
		Page<Tag> retreivedTags = tagService.list(null, searchFilter);
		
		assertThat("List tags by Group ID retrieved two tags", retreivedTags.getTotalElements(), is(2l));
        assertThat("there is 1 pages", retreivedTags.getTotalPages(), is(1));
        assertThat("we are on page 0", retreivedTags.getNumber(), is(0));
        assertThat("Tag Inserted is equal to tag received", retreivedTags.getContent().get(0).getName(), is("TagOne"));
        assertThat("Tag Inserted is equal to tag received", retreivedTags.getContent().get(1).getName(), is("TagTwo"));

        Tag tag1  = retreivedTags.getContent().get(0);
        Tag tag2  = retreivedTags.getContent().get(1);

        tag1.setName("TagThree");
       	tag2.setName("TagFour");
       	List<Tag> updatedList = new ArrayList<Tag>();
       	updatedList.add(tag1);
       	updatedList.add(tag2);
       	tagService.updateAll(updatedList);
       	
       	Page<Tag> retreivedUpdatedTags = tagService.list(null, searchFilter);
       	
       	assertThat("List tags by Group ID retrieved two tags", retreivedUpdatedTags.getTotalElements(), is(2l));
        assertThat("there is 1 pages", retreivedUpdatedTags.getTotalPages(), is(1));
        assertThat("we are on page 0", retreivedUpdatedTags.getNumber(), is(0));
        assertThat("Tag Inserted is equal to tag received", retreivedUpdatedTags.getContent().get(0).getName(), is("TagThree"));
        assertThat("Tag Inserted is equal to tag received", retreivedUpdatedTags.getContent().get(1).getName(), is("TagFour"));

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
	
	@Test
	public void deleteAllTags() {
		List<Tag> listOfTags = listOfTags();
		List<Tag> tags = tagService.saveAll(listOfTags);
		assertThat("Tags are persisted", tags.size(), is(notNullValue()));
		Long groupId = tags.get(0).getGroup().getId();

		tagService.removeAll(tags);

		Page<Tag> retrievedTagsPage = tagService.list(null, new TagSearchFilter(groupId));
		assertThat("retreivedTagsPage should be empty", retrievedTagsPage.getTotalElements(), is(0l));
	}
}
