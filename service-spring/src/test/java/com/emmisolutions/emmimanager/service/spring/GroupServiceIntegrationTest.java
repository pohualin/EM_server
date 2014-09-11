package com.emmisolutions.emmimanager.service.spring;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
import com.emmisolutions.emmimanager.model.GroupSaveRequest;
import com.emmisolutions.emmimanager.model.GroupSearchFilter;
import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.GroupService;
import com.emmisolutions.emmimanager.service.TagService;

/**
 * Group Service Integration Test
 *
 */
public class GroupServiceIntegrationTest extends BaseIntegrationTest {
	
	@Resource
    ClientService clientService;

    @Resource
    UserPersistence userPersistence;
    
	@Resource
    GroupService groupService;
	
	@Resource
    TagService tagService;
	
	private User superAdmin;

	@Before
	public void init() {
	    superAdmin = userPersistence.reload("super_admin");
	}
		
	/**
	 * 	Test list of groups by client id
	 */
	@Test
	public void testListGroupsByClientId(){

		Group groupOne = new Group();
		groupOne.setName("TestGroup1");
		Client clientOne = makeClient();
		clientService.create(clientOne);
		groupOne.setClient(clientService.reload(clientOne));
		groupOne = groupService.save(groupOne);

		Group groupTwo = new Group();
		groupTwo.setName("TestGroup2");
		groupTwo.setClient(clientService.reload(clientOne));
		groupTwo = groupService.save(groupTwo);
		
		GroupSearchFilter gsf = new GroupSearchFilter(clientOne.getId());
		Page<Group> groupPage = groupService.list(null, gsf);
		
		assertThat("found Page of groups for clientId:", groupPage.getTotalElements(), is(2l));
		assertThat("we are on page 0", groupPage.getNumber(), is(0));
	}
	
	private Client makeClient(){
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
	    return client;
	}
	
	/**
	 * 	Test group  by id
	 */
	@Test
	public void testGroupById(){
		Group group = new Group();
		group.setName("GroupNameA");
		Client clientOne = makeClient();
		clientService.create(clientOne);
		group.setClient(clientService.reload(clientOne));
		Group savedGroup = groupService.save(group);
		Group retrievedGroup = groupService.reload(savedGroup.getId());
		assertThat("Group get by ID successfully retrieved group:", retrievedGroup.getId(), is(savedGroup.getId()));
	}

	/**
	 * 	Test save groups with tags
	 */
	@Test
	public void saveGroupsWithTags(){
		
		List<GroupSaveRequest> groupSaveRequests = new ArrayList<GroupSaveRequest>();
		
		GroupSaveRequest groupSaveReqOne = new GroupSaveRequest();
		Group groupOne = new Group();
		groupOne.setName("GroupOne");
		Client clientOne = makeClient();
		clientService.create(clientOne);
		groupOne.setClient(clientService.reload(clientOne));
		Tag tagOneA = new Tag();
		tagOneA.setName("Tag OneA");
		Tag tagOneB = new Tag();
		tagOneB.setName("Tag OneB");
		List<Tag> listOne = new ArrayList<Tag>();
		listOne.add(tagOneA);
		listOne.add(tagOneB);

		groupSaveReqOne.setGroup(groupOne);
		groupSaveReqOne.setTags(listOne);
		
		
		GroupSaveRequest groupSaveReqTwo = new GroupSaveRequest();
		Group groupTwo = new Group();
		groupTwo.setName("GroupTwo");
		groupTwo.setClient(clientService.reload(clientOne));
		Tag tagTwoA = new Tag();
		tagTwoA.setName("Tag TwoA");
		Tag tagTwoB = new Tag();
		tagTwoB.setName("Tag OneB");
		List<Tag> listTwo = new ArrayList<Tag>();
		listTwo.add(tagTwoA);
		listTwo.add(tagTwoB);

		groupSaveReqTwo.setGroup(groupTwo);
		groupSaveReqTwo.setTags(listTwo);
		
		
		GroupSaveRequest groupSaveReqThree = new GroupSaveRequest();
		Group groupThree = new Group();
		groupThree.setName("GroupThree");
		groupThree.setClient(clientService.reload(clientOne));
		Tag tagThreeA = new Tag();
		tagThreeA.setName("Tag ThreeA");
		Tag tagThreeB = new Tag();
		tagThreeB.setName("Tag ThreeB");
		List<Tag> listThree = new ArrayList<Tag>();
		listThree.add(tagThreeA);
		listThree.add(tagThreeB);

		groupSaveReqThree.setGroup(groupThree);
		groupSaveReqThree.setTags(listThree);
		
		groupSaveRequests.add(groupSaveReqOne);
		groupSaveRequests.add(groupSaveReqTwo);
		groupSaveRequests.add(groupSaveReqThree);
				
		List<Group> groups = groupService.saveGroupsAndTags(groupSaveRequests,clientOne.getId());
		assertThat("groups were saved with tags", groups.size(), is(3));
		assertThat("groups were saved with tags", groups.get(0).getTags().iterator().next().getGroup(), is(notNullValue()));

		Tag tag = new Tag();
		tag.setId(groups.get(0).getTags().iterator().next().getId());;
		Tag savedTag = tagService.reload(tag);
		
		assertThat("Tag is saved with the Group ID entered", savedTag.getGroup().getId(), is(groups.get(0).getId()));

	}
	
	/**
	 * 	Test drop old groups and save new ones
	 */
	@Test
	public void saveGroupsWithTagsDropAllOldGroups(){
		
		List<GroupSaveRequest> groupSaveRequestsOne = new ArrayList<GroupSaveRequest>();
		
		GroupSaveRequest groupSaveReqOne = new GroupSaveRequest();
		Group groupOne = new Group();
		groupOne.setName("GroupOne");
		Client clientOne = makeClient();
		clientService.create(clientOne);
		groupOne.setClient(clientService.reload(clientOne));
		Tag tagOneA = new Tag();
		tagOneA.setName("Tag OneA");
		Tag tagOneB = new Tag();
		tagOneB.setName("Tag OneB");
		List<Tag> listOne = new ArrayList<Tag>();
		listOne.add(tagOneA);
		listOne.add(tagOneB);
		groupSaveReqOne.setGroup(groupOne);
		groupSaveReqOne.setTags(listOne);
		groupSaveRequestsOne.add(groupSaveReqOne);
		
		GroupSaveRequest groupSaveReqTwo = new GroupSaveRequest();
		Group groupTwo = new Group();
		groupTwo.setName("GroupTwo");
		groupTwo.setClient(clientService.reload(clientOne));
		Tag tagTwoA = new Tag();
		tagTwoA.setName("Tag TwoA");
		Tag tagTwoB = new Tag();
		tagTwoB.setName("Tag OneB");
		List<Tag> listTwo = new ArrayList<Tag>();
		listTwo.add(tagTwoA);
		listTwo.add(tagTwoB);
		groupSaveReqTwo.setGroup(groupTwo);
		groupSaveReqTwo.setTags(listTwo);
				
		GroupSaveRequest groupSaveReqThree = new GroupSaveRequest();
		Group groupThree = new Group();
		groupThree.setName("GroupThree");
		groupThree.setClient(clientService.reload(clientOne));
		Tag tagThreeA = new Tag();
		tagThreeA.setName("Tag ThreeA");
		Tag tagThreeB = new Tag();
		tagThreeB.setName("Tag ThreeB");
		List<Tag> listThree = new ArrayList<Tag>();
		listThree.add(tagThreeA);
		listThree.add(tagThreeB);
		groupSaveReqThree.setGroup(groupThree);
		groupSaveReqThree.setTags(listThree);
				
		List<GroupSaveRequest> groupSaveRequestsTwo = new ArrayList<GroupSaveRequest>();

		groupSaveRequestsTwo.add(groupSaveReqTwo);
		groupSaveRequestsTwo.add(groupSaveReqThree);
				
		List<Group> groups = groupService.saveGroupsAndTags(groupSaveRequestsOne,clientOne.getId());
		assertThat("group is saved", groups.size(), is(1));
		assertThat("groups is saved with tags", groups.get(0).getTags().iterator().next().getGroup(), is(notNullValue()));
		
		Page<Group> groupPage = groupService.list(null, new GroupSearchFilter(clientOne.getId()));
		assertThat("Total number of groups for the client before update is: ", groupPage.getTotalElements(), is(1l));
		
		Tag tag = new Tag();
		tag.setId(groups.get(0).getTags().iterator().next().getId());;
		Tag savedTag = tagService.reload(tag);
		
		assertThat("Tag is saved with the Group ID entered", savedTag.getGroup().getId(), is(groups.get(0).getId()));
		
		if (groupPage.hasContent() && !groupPage.getContent().isEmpty()) {
			groupService.removeAll(groupPage.getContent());
		}		
		
		List<Group> newGroups = groupService.saveGroupsAndTags(groupSaveRequestsTwo, clientOne.getId() );
		Page<Group> newGroupPage = groupService.list(null, new GroupSearchFilter(clientOne.getId()));
		assertThat("Total number of groups for the client after update is: ", newGroupPage.getTotalElements(), is(Long.valueOf(newGroups.size())));
		
		
	}

	/**
	 * 	Test save groups with no tags
	 */
	@Test
	public void saveGroupWithOptionalTags(){
		
		List<GroupSaveRequest> groupSaveRequests = new ArrayList<GroupSaveRequest>();
		
		GroupSaveRequest groupSaveReqOne = new GroupSaveRequest();
		Group groupOne = new Group();
		groupOne.setName("GroupOne");
		Client clientOne = makeClient();
		clientService.create(clientOne);
		groupOne.setClient(clientService.reload(clientOne));
		groupSaveReqOne.setGroup(groupOne);
		
		GroupSaveRequest groupSaveReqTwo = new GroupSaveRequest();
		Group groupTwo = new Group();
		groupTwo.setName("GroupTwo");
		groupTwo.setClient(clientService.reload(clientOne));
		Tag tagTwoA = new Tag();
		tagTwoA.setName("Tag TwoA");
		Tag tagTwoB = new Tag();
		tagTwoB.setName("Tag OneB");
		List<Tag> listTwo = new ArrayList<Tag>();
		listTwo.add(tagTwoA);
		listTwo.add(tagTwoB);

		groupSaveReqTwo.setGroup(groupTwo);
		groupSaveReqTwo.setTags(listTwo);
		
		GroupSaveRequest groupSaveReqThree = new GroupSaveRequest();
		Group groupThree = new Group();
		groupThree.setName("GroupThree");
		groupThree.setClient(clientService.reload(clientOne));
		Tag tagThreeA = new Tag();
		tagThreeA.setName("Tag ThreeA");
		Tag tagThreeB = new Tag();
		tagThreeB.setName("Tag ThreeB");
		List<Tag> listThree = new ArrayList<Tag>();
		listThree.add(tagThreeA);
		listThree.add(tagThreeB);

		groupSaveReqThree.setGroup(groupThree);
		groupSaveReqThree.setTags(listThree);
		
		groupSaveRequests.add(groupSaveReqOne);
		groupSaveRequests.add(groupSaveReqTwo);
		groupSaveRequests.add(groupSaveReqThree);
				
		List<Group> groups = groupService.saveGroupsAndTags(groupSaveRequests, clientOne.getId());
		assertThat("groups are saved with tags", groups.size(), is(3));

		Tag tag = new Tag();
		tag.setId(groups.get(1).getTags().iterator().next().getId());;
		Tag savedTag = tagService.reload(tag);
		
		assertThat("Tag is saved with the Group ID entered", savedTag.getGroup().getId(), is(groups.get(1).getId()));

	}
	
	/**
	 * 	Test tags are deleted on Group delete
	 */
	@Test(expected=NoSuchElementException.class)
	public void verifyTagsDeleteOnCascade(){
		
		List<GroupSaveRequest> groupSaveRequestOne = new ArrayList<GroupSaveRequest>();
		GroupSaveRequest groupSaveReqOne = new GroupSaveRequest();
		Group groupOne = new Group();
		groupOne.setName("GroupOne");
		Client clientOne = makeClient();
		clientService.create(clientOne);
		groupOne.setClient(clientService.reload(clientOne));
		groupSaveReqOne.setGroup(groupOne);
		Tag tagOneA = new Tag();
		tagOneA.setName("Tag OneA");
		Tag tagOneB = new Tag();
		tagOneB.setName("Tag OneB");
		List<Tag> listOne = new ArrayList<Tag>();
		listOne.add(tagOneA);
		listOne.add(tagOneB);
		groupSaveReqOne.setTags(listOne);
		groupSaveRequestOne.add(groupSaveReqOne);
				
		List<Group> groups = groupService.saveGroupsAndTags(groupSaveRequestOne, clientOne.getId());
		assertThat("Group is saved:", groups.size(), is(1));

		Tag tag = new Tag();
		tag.setId(groups.get(0).getTags().iterator().next().getId());;
		Tag savedTag = tagService.reload(tag);
		assertThat("Tag is saved with the expected GroupId", savedTag.getGroup().getId(), is(groups.get(0).getId()));
		assertThat("Two tags were saved", groups.get(0).getTags().size(), is(listOne.size()));

		List<GroupSaveRequest> groupSaveRequestTwo = new ArrayList<GroupSaveRequest>();
		GroupSaveRequest groupSaveReqTwo = new GroupSaveRequest();
		Group groupTwo = new Group();
		groupTwo.setName("GroupTwo");
		groupTwo.setClient(clientService.reload(clientOne));

		groupSaveReqTwo.setGroup(groupTwo);
		groupSaveRequestTwo.add(groupSaveReqTwo);
		
		List<Group> updatedGroups = groupService.saveGroupsAndTags(groupSaveRequestTwo, clientOne.getId());
		assertThat("group was saved with no tags", updatedGroups.size(), is(1));
		assertThat("Verify that tag does not exist, expected NoSuchElementException", updatedGroups.get(0).getTags().iterator().next().getId(), is(notNullValue()));

	}
}
