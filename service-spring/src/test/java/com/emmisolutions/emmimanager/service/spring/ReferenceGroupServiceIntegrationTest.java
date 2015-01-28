package com.emmisolutions.emmimanager.service.spring;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Test;

import com.emmisolutions.emmimanager.model.RefGroupSaveRequest;
import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.model.ReferenceGroupType;
import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.emmisolutions.emmimanager.persistence.repo.ReferenceGroupTypeRepository;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ReferenceGroupService;
import com.emmisolutions.emmimanager.service.ReferenceTagService;

/**
 * Integration tests for ReferenceGroupService
 */
public class ReferenceGroupServiceIntegrationTest extends BaseIntegrationTest {

    @Resource
    ReferenceGroupService referenceGroupService;
    
    @Resource
    ReferenceTagService referenceTagService;
    
    @Resource 
    ReferenceGroupTypeRepository referenceGroupTypeRepository;
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
    @Test (expected = IllegalArgumentException.class)
    public void createNewReferenceGroupWithNoTags(){
        List<RefGroupSaveRequest> refGroupSaveRequests = new ArrayList<>();
        ReferenceGroupType groupType = new ReferenceGroupType();
        groupType.setName("Film");
        groupType= referenceGroupTypeRepository.save(groupType);
        RefGroupSaveRequest groupSaveReqOne = new RefGroupSaveRequest();        
        ReferenceGroup group = new ReferenceGroup();
        group.setName("New Wave");
        group.setType(groupType);
        groupSaveReqOne.setReferenceGroup(group);
        refGroupSaveRequests.add(groupSaveReqOne);
        Set<ReferenceGroup> groups = referenceGroupService.saveReferenceGroupsAndReferenceTags(refGroupSaveRequests);
        assertThat("Reference Groups are saved", groups.size(), is(1));
    }
    
    /**
     * Delete of a reference tag
     */
    @Test
    public void deleteReferenceTag(){
        Set<ReferenceGroup> groups = referenceGroupService.saveReferenceGroupsAndReferenceTags(refGroupWithTags());
        assertThat("One Reference Group is saved: ", groups.size(), is(1));
        assertThat("Three Reference Tags are saved: ", groups.iterator().next().getTags().size(), is(3));
        referenceTagService.deleteReferenceTag(groups.iterator().next().getTags().iterator().next());
        Set<ReferenceTag> tags2 = referenceTagService.findAllTagsByGroup(groups.iterator().next());
        assertThat("Updated number of tags is: ", tags2.size(), is(2));
    }

    /**
     * Test edit of a group name
     */
    @Test
    public void editReferenceGroupTitle(){
        List<RefGroupSaveRequest> refGroupSaveRequests = new ArrayList<>();
        List<ReferenceTag> tagList = new ArrayList<>();

        ReferenceGroupType groupType = new ReferenceGroupType();
        groupType.setName("REFERENCEDATA");
        groupType= referenceGroupTypeRepository.save(groupType);

        RefGroupSaveRequest groupSaveReqOne = new RefGroupSaveRequest();        
        ReferenceGroup group = new ReferenceGroup();
        group.setName("Godard Retrospective");
        group.setType(groupType);
        ReferenceTag tagOne = new ReferenceTag();
        tagOne.setName("Breathless");
        ReferenceTag tagTwo = new ReferenceTag();
        tagTwo.setName("Peirrot Le Fou");
        tagList.add(tagOne);
        tagList.add(tagTwo);
        groupSaveReqOne.setReferenceGroup(group);
        groupSaveReqOne.setReferenceTags(tagList);
        refGroupSaveRequests.add(groupSaveReqOne);

        Set<ReferenceGroup> groups = referenceGroupService.saveReferenceGroupsAndReferenceTags(refGroupSaveRequests);
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

        ReferenceGroupType groupType = new ReferenceGroupType();
        groupType.setName("REFERENCE DATA");
        groupType= referenceGroupTypeRepository.save(groupType);

        RefGroupSaveRequest groupSaveReqOne = new RefGroupSaveRequest();        
        ReferenceGroup group = new ReferenceGroup();
        group.setName("Godard");
        group.setType(groupType);
        ReferenceTag tagOne = new ReferenceTag();
        tagOne.setName("Made In Usa");
        ReferenceTag tagTwo = new ReferenceTag();
        tagTwo.setName("Alphaville");
        ReferenceTag tagThree = new ReferenceTag();
        tagThree.setName("Band Of Outsiders");
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
        Set<ReferenceGroup> groups = referenceGroupService.saveReferenceGroupsAndReferenceTags(refGroupWithTags());
        assertThat("One Reference Group is saved: ", groups.size(), is(1));
    }
    
    /**
     * test adding a new tag to an existing ref group
     */
    @Test
    public void addNewReferenceTagToExistingReferenceGroup(){
        Set<ReferenceGroup> groups = referenceGroupService.saveReferenceGroupsAndReferenceTags(refGroupWithTags());
        assertThat("One Reference Group is saved: ", groups.size(), is(1));
        assertThat("Three Reference Tags is saved: ", groups.iterator().next().getTags().size(), is(3));

        ReferenceGroup existingGroup = groups.iterator().next();
        ReferenceTag tagToAdd = new ReferenceTag();
        tagToAdd.setName("A Woman Is A Woman");
        tagToAdd.setGroup(existingGroup);
        ReferenceTag savedTag = referenceTagService.create(tagToAdd);
        assertThat("One Reference Group is saved: ",savedTag.getId(), is(notNullValue()));

        Set<ReferenceTag> tags = referenceTagService.findAllTagsByGroup(existingGroup);
        assertThat("Updated number of tags is: ", tags.size(), is(4));
    }
}
