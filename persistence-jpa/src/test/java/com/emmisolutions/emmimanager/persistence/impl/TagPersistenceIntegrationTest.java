package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.persistence.*;
import com.emmisolutions.emmimanager.persistence.repo.ClientTypeRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
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
    UserAdminPersistence userAdminPersistence;

	UserAdmin superAdmin;

    @Resource
    ClientTypeRepository clientTypeRepository;

    ClientType clientType;

    @Before
    public void init() {
        superAdmin = userAdminPersistence.reload("super_admin");
        clientType = clientTypeRepository.getOne(1l);
    }

    /**
     * 	Test list of tags by group id
     */
    @Test
    public void testListTagsByGroupId(){
        Group group = createGroup(createClient(0), "TestGroup");

        Tag tagOne = new Tag();
        Tag tagTwo = new Tag();
        tagOne.setName("TagOne");
        tagTwo.setName("TagTwo");
        tagOne.setGroup(group);
        tagTwo.setGroup(group);

        tagPersistence.save(tagOne);
        tagOne = tagPersistence.reload(tagOne);

        List<Tag> tags = new ArrayList<>();
        tags.add(tagTwo);
        tagPersistence.createAll(tags);

        TagSearchFilter searchFilter = new TagSearchFilter(group.getId());
        Page<Tag> retrievedTags = tagPersistence.listTagsByGroupId(null, searchFilter);

        assertThat("List tags by Group ID retrieved two tags", retrievedTags.getTotalElements(), is(2l));
        assertThat("there is 1 pages", retrievedTags.getTotalPages(), is(1));
        assertThat("we are on page 0", retrievedTags.getNumber(), is(0));
        assertThat("Tag Inserted is equal to tag received", retrievedTags, hasItems(tagOne, tagTwo));
        assertThat("Tag belongs to the group", retrievedTags.getContent().iterator().next().getGroup(), is(group));
    }

    /**
     * Reload of null tag returns null
     */
    @Test
    public void testReloadNull(){
        assertThat("Null reload returns null", tagPersistence.reload(null), is(nullValue()));
    }

    /**
     *  Remove nothing should remove zero
     */
    @Test
    public void removeNull(){
        assertThat("Removal of no tags should be zero", tagPersistence.removeTagsThatAreNotAssociatedWith(null, null), is(0l));
    }

	/**
	 * 	Remove tags for other groups in the client
	 */
	@Test
	public void remove(){
        // client 1, group 1, tag 1
        Client client1 = createClient(1);
        Group group1 = createGroup(client1, "group 1");
        final Tag tagOne = new Tag();
        tagOne.setName("TagOne");
        tagOne.setGroup(group1);

        // client 2, with two groups
        Client client2 = createClient(2);
        Group group2 = createGroup(client2, "group 2");
        final Tag tagTwo = new Tag();
        tagTwo.setName("TagTwo");
        tagTwo.setGroup(group2);
        final Tag tagThree = new Tag();
        tagThree.setName("TagThree");
        tagThree.setGroup(group2);
        final Group group2a = createGroup(client2, "group 2a");
        final Tag tagTwoA = new Tag();
        tagTwoA.setName("TagTwoA");
        tagTwoA.setGroup(group2a);

        // save the tags
		List<Tag> tags = new ArrayList<Tag>(){{
            add(tagOne);
            add(tagTwo);
            add(tagTwoA);
            add(tagThree);
        }};
		tagPersistence.createAll(tags);

        final Long notInTheDatabase = 1224l;

        // do the action to test, remove all tags that are not in group2a for client2
        assertThat("should have removed two tags", tagPersistence.removeTagsThatAreNotAssociatedWith(client2.getId(), new HashSet<Long>(){{
            add(group2a.getId());
            add(notInTheDatabase); // this shouldn't break things
        }}), is(2l));
        assertThat("All tags should be removed for group2",
                tagPersistence.listTagsByGroupId(null, new TagSearchFilter(group2.getId())).getTotalElements(), is(0l));

        // group 2a should be intact
        Page<Tag> tagsOnGroup2a = tagPersistence.listTagsByGroupId(null, new TagSearchFilter(group2a.getId()));
        assertThat("Tags should still be there for group2a",
                tagsOnGroup2a.getTotalElements(), is(1l));
        assertThat("should still have tagTwoA", tagsOnGroup2a, hasItem(tagTwoA));

        // remove at the client level
        assertThat("should have removed one tag", tagPersistence.removeTagsThatAreNotAssociatedWith(client2.getId(), null), is(1l));
        tagsOnGroup2a = tagPersistence.listTagsByGroupId(null, new TagSearchFilter(group2a.getId()));
        assertThat("Tags should now be gone for group2a", tagsOnGroup2a.getTotalElements(), is(0l));

        // group 1 should be intact
        Page<Tag> tagsOnGroup1 = tagPersistence.listTagsByGroupId(null, new TagSearchFilter(group1.getId()));
        assertThat("Tags should still be there for group1", tagsOnGroup1.getTotalElements(), is(1l));
        assertThat("should still have tagOne", tagsOnGroup1, hasItem(tagOne));


	}

    private Client createClient(int id){
        Client client = new Client();
        client.setTier(new ClientTier(3l));
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setContractStart(LocalDate.now());
        client.setRegion(new ClientRegion(1l));
        client.setName("Tag Persistence" + id);
        client.setType(clientType);
        client.setActive(false);
        client.setContractOwner(superAdmin);
        client.setSalesForceAccount(new SalesForce(RandomStringUtils.randomAlphanumeric(18)));
        return clientPersistence.save(client);
    }

	private Group createGroup(Client client, String name){
		Group group = new Group();
		group.setName(name);
		group.setClient(client);
		group = groupPersistence.save(group);
		return group;
	}
}
