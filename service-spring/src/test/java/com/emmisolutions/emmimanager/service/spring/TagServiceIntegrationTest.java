package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.GroupService;
import com.emmisolutions.emmimanager.service.TagService;
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
 * Tag service integration Test
 */
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
		client.setRegion(new ClientRegion(1l));
		client.setName("Test Client" + System.currentTimeMillis());
		client.setType(new ClientType(3l));
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
	
	
	/**
 	* 	Test List tags by group id
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
		tagService.saveAllTagsForGroup(tags, group);
		
		TagSearchFilter searchFilter = new TagSearchFilter(group.getId());
		Page<Tag> retreivedTags = tagService.list(null, searchFilter);
		
		assertThat("List tags by Group ID retrieved two tags", retreivedTags.getTotalElements(), is(2l));
        assertThat("There is 1 page", retreivedTags.getTotalPages(), is(1));
        assertThat("We are on page 0", retreivedTags.getNumber(), is(0));
        assertThat("Tag Inserted is equal to tag received", retreivedTags.getContent().get(0).getName(), is("TagOne"));
        assertThat("Tag Inserted is equal to tag received", retreivedTags.getContent().get(1).getName(), is("TagTwo"));
        assertThat("Tag belongs to the group", retreivedTags.getContent().iterator().next().getGroup().getId(), is(group.getId()));

	}
	
	@Test
	public void testTagSave(){
		
		Group group = createGroup();
		group.setName("TestGroup");
		group = groupService.save(group);

		List<Tag> tags = new ArrayList<Tag>();

		Tag one = new Tag();
		Tag two = new Tag();
		two.setName("two");
		tags.add(one);
		tags.add(two);

		List<Tag> savedTags = tagService.saveAllTagsForGroup(tags, group);
		assertThat("Only one tag with valid tagName was saved", savedTags.size(), is(1));
	}
}
