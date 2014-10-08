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
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Group Service Integration Test
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
     * Test list of groups by client id
     */
    @Test
    public void testListGroupsByClientId() {

        Group groupOne = new Group();
        groupOne.setName("TestGroup1");
        Client clientOne = makeClient(1);
        clientService.create(clientOne);
        groupOne.setClient(clientService.reload(clientOne));
        groupService.save(groupOne);

        Group groupTwo = new Group();
        groupTwo.setName("TestGroup2");
        groupTwo.setClient(clientService.reload(clientOne));
        groupService.save(groupTwo);

        GroupSearchFilter gsf = new GroupSearchFilter(clientOne.getId());
        Page<Group> groupPage = groupService.list(gsf);

        assertThat("found Page of groups for clientId:", groupPage.getTotalElements(), is(2l));
        assertThat("we are on page 0", groupPage.getNumber(), is(0));
    }

    private Client makeClient(int id) {
        Client client = new Client();
        client.setTier(ClientTier.THREE);
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setContractStart(LocalDate.now());
        client.setRegion(ClientRegion.NORTHEAST);
        client.setName("Test Client " + id);
        client.setType(new ClientType(1l));
        client.setActive(false);
        client.setContractOwner(superAdmin);
        client.setSalesForceAccount(new SalesForce("xxxWW" + System.currentTimeMillis()));
        return client;
    }

    /**
     * Test group  by id
     */
    @Test
    public void testGroupById() {
        Group group = new Group();
        group.setName("GroupNameA");
        Client clientOne = makeClient(2);
        clientService.create(clientOne);
        group.setClient(clientService.reload(clientOne));
        Group savedGroup = groupService.save(group);
        Group retrievedGroup = groupService.reload(savedGroup.getId());
        assertThat("Group get by ID successfully retrieved group:", retrievedGroup.getId(), is(savedGroup.getId()));
    }

    /**
     * Test save groups with tags
     */
    @Test
    public void saveGroupsWithTags() {

        List<GroupSaveRequest> groupSaveRequests = new ArrayList<>();

        GroupSaveRequest groupSaveReqOne = new GroupSaveRequest();
        Group groupOne = new Group();
        groupOne.setName("GroupOne");
        Client clientOne = makeClient(3);
        clientService.create(clientOne);
        groupOne.setClient(clientService.reload(clientOne));
        Tag tagOneA = new Tag();
        tagOneA.setName("Tag OneA");
        Tag tagOneB = new Tag();
        tagOneB.setName("Tag OneB");
        List<Tag> listOne = new ArrayList<>();
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
        List<Tag> listTwo = new ArrayList<>();
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
        List<Tag> listThree = new ArrayList<>();
        listThree.add(tagThreeA);
        listThree.add(tagThreeB);

        groupSaveReqThree.setGroup(groupThree);
        groupSaveReqThree.setTags(listThree);

        groupSaveRequests.add(groupSaveReqOne);
        groupSaveRequests.add(groupSaveReqTwo);
        groupSaveRequests.add(groupSaveReqThree);

        Set<Group> groups = groupService.saveGroupsAndTags(groupSaveRequests, clientOne.getId());
        assertThat("groups were saved with tags", groups.size(), is(3));
        assertThat("groups were saved with tags", groups.iterator().next().getTags().iterator().next().getGroup(), is(notNullValue()));

        Tag tag = new Tag();
        tag.setId(groups.iterator().next().getTags().iterator().next().getId());
        Tag savedTag = tagService.reload(tag);

        assertThat("Tag is saved with the Group ID entered", savedTag.getGroup().getId(), is(groups.iterator().next().getId()));

    }

    /**
     * Test drop old groups and save new ones
     */
    @Test
    public void saveGroupsWithTagsDropAllOldGroups() {

        List<GroupSaveRequest> groupSaveRequestsOne = new ArrayList<>();

        GroupSaveRequest groupSaveReqOne = new GroupSaveRequest();
        Group groupOne = new Group();
        groupOne.setName("GroupOne");
        Client clientOne = makeClient(4);
        clientService.create(clientOne);
        groupOne.setClient(clientService.reload(clientOne));
        Tag tagOneA = new Tag();
        tagOneA.setName("Tag OneA");
        Tag tagOneB = new Tag();
        tagOneB.setName("Tag OneB");
        List<Tag> listOne = new ArrayList<>();
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
        List<Tag> listTwo = new ArrayList<>();
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
        List<Tag> listThree = new ArrayList<>();
        listThree.add(tagThreeA);
        listThree.add(tagThreeB);
        groupSaveReqThree.setGroup(groupThree);
        groupSaveReqThree.setTags(listThree);

        List<GroupSaveRequest> groupSaveRequestsTwo = new ArrayList<>();

        groupSaveRequestsTwo.add(groupSaveReqTwo);
        groupSaveRequestsTwo.add(groupSaveReqThree);

        Set<Group> groups = groupService.saveGroupsAndTags(groupSaveRequestsOne, clientOne.getId());
        assertThat("group is saved", groups.size(), is(1));
        assertThat("groups is saved with tags", groups.iterator().next().getTags().iterator().next().getGroup(), is(notNullValue()));

        Page<Group> groupPage = groupService.list(null, new GroupSearchFilter(clientOne.getId()));
        assertThat("Total number of groups for the client before update is: ", groupPage.getTotalElements(), is(1l));

        Tag tag = new Tag();
        tag.setId(groups.iterator().next().getTags().iterator().next().getId());
        Tag savedTag = tagService.reload(tag);

        assertThat("Tag is saved with the Group ID entered", savedTag.getGroup().getId(), is(groups.iterator().next().getId()));

        Set<Group> newGroups = groupService.saveGroupsAndTags(groupSaveRequestsTwo, clientOne.getId());
        Page<Group> newGroupPage = groupService.list(null, new GroupSearchFilter(clientOne.getId()));
        assertThat("Total number of groups for the client after update is: ", newGroupPage.getTotalElements(), is((long) newGroups.size()));


    }

    /**
     * Test save groups with no tags
     */
    @Test(expected = IllegalArgumentException.class)
    public void saveGroupWithNoTags() {

        List<GroupSaveRequest> groupSaveRequests = new ArrayList<>();

        GroupSaveRequest groupSaveReqOne = new GroupSaveRequest();
        Group groupOne = new Group();
        groupOne.setName("GroupOne");
        Client clientOne = makeClient(5);
        clientService.create(clientOne);
        groupOne.setClient(clientService.reload(clientOne));
        groupSaveReqOne.setGroup(groupOne);

        groupSaveRequests.add(groupSaveReqOne);
        Set<Group> groups = groupService.saveGroupsAndTags(groupSaveRequests, clientOne.getId());
        assertThat("groups are saved with tags", groups.size(), is(3));

        Tag tag = new Tag();
        tag.setId(groups.iterator().next().getTags().iterator().next().getId());
        Tag savedTag = tagService.reload(tag);

        assertThat("Tag is saved with the Group ID entered", savedTag.getGroup().getId(), is(groups.iterator().next().getId()));

    }

    @Test
    public void saveWithoutAClient(){
        assertThat("An empty set should come back when saving without a client", groupService.saveGroupsAndTags(new ArrayList<GroupSaveRequest>(), Long.MAX_VALUE).isEmpty(), is(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveGroupsWithTheSameName() {
        final Tag tagThreeA = new Tag();
        tagThreeA.setName("Tag ThreeA");
        final Tag tagThreeB = new Tag();
        tagThreeB.setName("Tag ThreeB");
        List<Tag> tags = new ArrayList<Tag>(){{
            add(tagThreeA);
            add(tagThreeB);
        }};

        final GroupSaveRequest groupOneSaveRequest = new GroupSaveRequest();
        Group groupOne = new Group();
        groupOne.setName("GroupOne");
        groupOneSaveRequest.setGroup(groupOne);
        groupOneSaveRequest.setTags(tags);

        final GroupSaveRequest groupTwoSaveRequest = new GroupSaveRequest();
        Group groupTwo = new Group();
        groupTwo.setName("Group--@#$%@#$^& One%^&&^^&&%%$%$%#");
        groupTwoSaveRequest.setGroup(groupTwo);
        groupTwoSaveRequest.setTags(tags);

        groupService.saveGroupsAndTags(new ArrayList<GroupSaveRequest>() {{
            add(groupOneSaveRequest);
            add(groupTwoSaveRequest);
        }}, clientService.create(makeClient(6)).getId());
    }
}
