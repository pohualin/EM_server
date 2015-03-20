package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Group;
import com.emmisolutions.emmimanager.model.RefGroupSaveRequest;
import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.GroupService;
import com.emmisolutions.emmimanager.service.ReferenceGroupService;
import com.emmisolutions.emmimanager.service.ReferenceTagService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration tests for ReferenceGroupService
 */
public class ReferenceGroupServiceIntegrationTest extends BaseIntegrationTest {

    @Resource
    ReferenceGroupService referenceGroupService;
    
    @Resource
    ReferenceTagService referenceTagService;

    @Resource
    GroupService groupService;
    
    /**
     * Make sure we can load a page of reference groups
     */
    @Test
    public void load() {
        assertThat("Reference Groups are loaded",
            referenceGroupService.loadReferenceGroups(null).getTotalElements(), is(not(0l)));
    }

    /**
     * Saving a reference group with no tags should throw an error
     */
    @Test (expected = InvalidDataAccessApiUsageException.class)
    public void createNewReferenceGroupWithNoTags(){
        RefGroupSaveRequest groupSaveReqOne = new RefGroupSaveRequest();        
        ReferenceGroup group = new ReferenceGroup();
        group.setName(RandomStringUtils.randomAlphanumeric(18));
        groupSaveReqOne.setReferenceGroup(group);
        referenceGroupService.saveReferenceGroupAndReferenceTags(groupSaveReqOne);
    }

    /**
     * Test edit of a group name
     */
    @Test
    public void editReferenceGroupTitle(){
        List<RefGroupSaveRequest> refGroupSaveRequests = new ArrayList<>();
        List<ReferenceTag> tagList = new ArrayList<>();


        RefGroupSaveRequest groupSaveReqOne = new RefGroupSaveRequest();        
        ReferenceGroup group = new ReferenceGroup();
        group.setName("Godard Retrospective");
        ReferenceTag tagOne = new ReferenceTag();
        tagOne.setName("Breathless");
        ReferenceTag tagTwo = new ReferenceTag();
        tagTwo.setName("Peirrot Le Fou");
        tagList.add(tagOne);
        tagList.add(tagTwo);
        groupSaveReqOne.setReferenceGroup(group);
        groupSaveReqOne.setReferenceTags(tagList);
        refGroupSaveRequests.add(groupSaveReqOne);

        Set<ReferenceGroup> groups = new HashSet<>();
        for(RefGroupSaveRequest request: refGroupSaveRequests){
            ReferenceGroup savedGroup = referenceGroupService.saveReferenceGroupAndReferenceTags(request);
            groups.add(savedGroup);
        }
        assertThat("Reference Group is saved", groups.size(), is(1));
        assertThat("The name of original group saved is: ", groups.iterator().next().getName(), is("Godard Retrospective"));
        
        ReferenceGroup fromDb = referenceGroupService.reload(groups.iterator().next().getId());
        fromDb.setName("Jean-Luc Godard");
        
        ReferenceGroup updatedGroupName = referenceGroupService.updateReferenceGroup(fromDb);
        assertThat("The updated name of group saved is: ", updatedGroupName.getName(), is("Jean-Luc Godard"));
    }
    
    private List<RefGroupSaveRequest> refGroupWithTags(){
        List<RefGroupSaveRequest> refGroupSaveRequests = new ArrayList<>();
        List<ReferenceTag> tagList = new ArrayList<>();

        RefGroupSaveRequest groupSaveReqOne = new RefGroupSaveRequest();        
        ReferenceGroup group = new ReferenceGroup();
        group.setName(RandomStringUtils.randomAlphanumeric(11));
        ReferenceTag tagOne = new ReferenceTag();
        tagOne.setName(RandomStringUtils.randomAlphanumeric(12));
        ReferenceTag tagTwo = new ReferenceTag();
        tagTwo.setName(RandomStringUtils.randomAlphanumeric(13));
        ReferenceTag tagThree = new ReferenceTag();
        tagThree.setName(RandomStringUtils.randomAlphanumeric(14));
        tagList.add(tagOne);
        tagList.add(tagTwo);
        tagList.add(tagThree);
        groupSaveReqOne.setReferenceGroup(group);
        groupSaveReqOne.setReferenceTags(tagList);
        refGroupSaveRequests.add(groupSaveReqOne);
        return refGroupSaveRequests;
    }
    
    /**
     * test create of a new ref group with tags
     */
    @Test
    public void createNewReferenceGroupWithReferenceTags(){
        Set<ReferenceGroup> groups = new HashSet<>();
        for(RefGroupSaveRequest request: refGroupWithTags()){
            ReferenceGroup savedGroup = referenceGroupService.saveReferenceGroupAndReferenceTags(request);
            groups.add(savedGroup);
        }
        assertThat("One Reference Group is saved: ", groups.size(), is(1));
    }
    
    /**
     * update groups
     */
    @Test
    public void updateGroups(){
        Set<ReferenceGroup> groups = new HashSet<>();
        for(RefGroupSaveRequest request: refGroupWithTags()){
            ReferenceGroup savedGroup = referenceGroupService.saveReferenceGroupAndReferenceTags(request);
            groups.add(savedGroup);
        }
        assertThat("One group was saved: ", groups.size(), is(1));

        ReferenceGroup one = groups.iterator().next();

        List<RefGroupSaveRequest> refGroupSaveRequests = new ArrayList<>();
        List<ReferenceTag> tagList = new ArrayList<>();
        RefGroupSaveRequest groupSaveReqOne = new RefGroupSaveRequest();        
        ReferenceGroup group = new ReferenceGroup();
        group.setName(RandomStringUtils.randomAlphanumeric(14));
        ReferenceTag tagOne = new ReferenceTag();
        tagOne.setName(RandomStringUtils.randomAlphanumeric(15));
        ReferenceTag tagTwo = new ReferenceTag();
        tagTwo.setName(RandomStringUtils.randomAlphanumeric(13));
        tagList.add(tagOne);
        tagList.add(tagTwo);
        groupSaveReqOne.setReferenceGroup(group);
        groupSaveReqOne.setReferenceTags(tagList);
        
        RefGroupSaveRequest groupSaveReqTwo = new RefGroupSaveRequest();        
        groupSaveReqTwo.setReferenceGroup(groups.iterator().next());
        groupSaveReqTwo.setReferenceTags(new ArrayList<>(groups.iterator().next().getTags()));
        
        refGroupSaveRequests.add(groupSaveReqOne);
        refGroupSaveRequests.add(groupSaveReqTwo);
        Set<ReferenceGroup> refreshedGroups = new HashSet<>();
        for(RefGroupSaveRequest request: refGroupSaveRequests){
            ReferenceGroup savedGroup = referenceGroupService.saveReferenceGroupAndReferenceTags(request);
            refreshedGroups.add(savedGroup);
        }
        assertThat("Updated number of tags is: ", refreshedGroups.size(), is(2));
        
        for (ReferenceGroup group1: refreshedGroups){
            if (group1.getId().equals(one.getId())){
            assertThat("tags remained equal:", group1.getTags().size(), is(3));
            }
        }
    }
    
    /**
     * test update group and delete tags
     */
    @Test
    public void updateGroupsWithDelete(){
        Set<ReferenceGroup> groups = new HashSet<>();
        for(RefGroupSaveRequest request: refGroupWithTags()){
            ReferenceGroup savedGroup = referenceGroupService.saveReferenceGroupAndReferenceTags(request);
            groups.add(savedGroup);
        }
        assertThat("Saved one group: ", groups.size(), is(1));

        ReferenceGroup one = groups.iterator().next();
        
        final ReferenceTag newTag = new ReferenceTag();
        newTag.setName(RandomStringUtils.randomAlphanumeric(18));

        List<RefGroupSaveRequest> refGroupSaveRequests = new ArrayList<>();
        List<ReferenceTag> tagList = new ArrayList<>();
        RefGroupSaveRequest groupSaveReqOne = new RefGroupSaveRequest();        
        ReferenceGroup group = new ReferenceGroup();
        group.setName(RandomStringUtils.randomAlphanumeric(16));
        ReferenceTag tagOne = new ReferenceTag();
        tagOne.setName(RandomStringUtils.randomAlphanumeric(17));
        ReferenceTag tagTwo = new ReferenceTag();
        tagTwo.setName(RandomStringUtils.randomAlphanumeric(18));
        tagList.add(tagOne);
        tagList.add(tagTwo);
        groupSaveReqOne.setReferenceGroup(group);
        groupSaveReqOne.setReferenceTags(tagList);
        
        RefGroupSaveRequest groupSaveReqTwo = new RefGroupSaveRequest();        
        groupSaveReqTwo.setReferenceGroup(one);
        groupSaveReqTwo.setReferenceTags(new ArrayList<ReferenceTag>() {{ 
            add(newTag); 
            }});
        
        refGroupSaveRequests.add(groupSaveReqOne);
        refGroupSaveRequests.add(groupSaveReqTwo);
        Set<ReferenceGroup> refreshedGroups = new HashSet<>();
        for(RefGroupSaveRequest request: refGroupSaveRequests){
            ReferenceGroup savedGroup = referenceGroupService.saveReferenceGroupAndReferenceTags(request);
            refreshedGroups.add(savedGroup);
        }
        assertThat("Two groups saved: ", refreshedGroups.size(), is(2));
        
        for (ReferenceGroup group1: refreshedGroups){
            if (group1.getId().equals(one.getId())){
            assertThat("tags remained equal:", group1.getTags().size(), is(1));
            }
        }
    }

    /**
     * Get all tags for a group
     */
    @Test
    public void testGetAllTagsForGroup(){
        RefGroupSaveRequest groupSaveReqOne = new RefGroupSaveRequest();        
        ReferenceGroup group = new ReferenceGroup();
        group.setName(RandomStringUtils.randomAlphanumeric(8));
        final ReferenceTag tagOne = new ReferenceTag();
        tagOne.setName(RandomStringUtils.randomAlphanumeric(8));
        groupSaveReqOne.setReferenceGroup(group);
        groupSaveReqOne.setReferenceTags(new ArrayList<ReferenceTag>() {{
            add(tagOne);
        }}
        );
        ReferenceGroup savedGroup = referenceGroupService.saveReferenceGroupAndReferenceTags(groupSaveReqOne);
        Pageable page = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        Page<ReferenceTag> tags = referenceTagService.findAllTagsByGroup(savedGroup, page);
        assertThat("Page of tags returned:", tags.getTotalElements(), is(1l));
        Page<ReferenceTag> tagsWithNoPageLimit = referenceTagService.findAllTagsByGroup(savedGroup, null);
        assertThat("Page of tags returned:", tagsWithNoPageLimit.getTotalElements(), is(1l));

    }

    /**
     * make sure the can delete evaluates properly
     */
    @Test
    public void canDelete() {
        ReferenceGroup referenceGroup = referenceGroupService.loadReferenceGroups(null).iterator().next();
        Group group = makeNewRandomGroup(null);
        assertThat("should still be deletable", referenceGroupService.isDeletable(referenceGroup), is(true));
        group.setType(referenceGroup.getType());
        groupService.save(group);
        assertThat("should not be deletable", referenceGroupService.isDeletable(referenceGroup), is(false));
    }

    /**
     * can't delete nothing
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void badDelete() {
        referenceGroupService.delete(null);
    }

    /**
     * Make sure delete doesn't stack out
     */
    @Test
    public void goodDelete() {
        RefGroupSaveRequest groupSaveReqOne = new RefGroupSaveRequest();
        ReferenceGroup group = new ReferenceGroup();
        group.setName(RandomStringUtils.randomAlphanumeric(8));
        final ReferenceTag tagOne = new ReferenceTag();
        tagOne.setName(RandomStringUtils.randomAlphanumeric(8));
        groupSaveReqOne.setReferenceGroup(group);
        groupSaveReqOne.setReferenceTags(new ArrayList<ReferenceTag>() {{
                                             add(tagOne);
                                         }}
        );
        ReferenceGroup savedGroup = referenceGroupService.saveReferenceGroupAndReferenceTags(groupSaveReqOne);
        referenceGroupService.delete(savedGroup);
    }

}
