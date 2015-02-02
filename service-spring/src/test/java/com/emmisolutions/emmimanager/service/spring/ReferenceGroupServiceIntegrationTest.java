package com.emmisolutions.emmimanager.service.spring;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;

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
    @Test (expected = InvalidDataAccessApiUsageException.class)
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
        Set<ReferenceGroup> groups = new HashSet();
        for(RefGroupSaveRequest request: refGroupSaveRequests){
            ReferenceGroup savedGroup = referenceGroupService.saveReferenceGroupAndReferenceTags(request);
            groups.add(savedGroup);
        }
        
        assertThat("Reference Groups are saved", groups.size(), is(1));
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

        Set<ReferenceGroup> groups = new HashSet();
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
        Set<ReferenceGroup> groups = new HashSet();
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
        Set<ReferenceGroup> groups = new HashSet();
        for(RefGroupSaveRequest request: refGroupWithTags()){
            ReferenceGroup savedGroup = referenceGroupService.saveReferenceGroupAndReferenceTags(request);
            groups.add(savedGroup);
        }
        assertThat("One group was saved: ", groups.size(), is(1));

        ReferenceGroup one = groups.iterator().next();

        List<RefGroupSaveRequest> refGroupSaveRequests = new ArrayList<>();
        List<ReferenceTag> tagList = new ArrayList<>();

        ReferenceGroupType groupType = new ReferenceGroupType();
        groupType.setName("REFERENCE DATA");
        groupType= referenceGroupTypeRepository.save(groupType);

        RefGroupSaveRequest groupSaveReqOne = new RefGroupSaveRequest();        
        ReferenceGroup group = new ReferenceGroup();
        group.setName("Francois Truffaut");
        group.setType(groupType);
        ReferenceTag tagOne = new ReferenceTag();
        tagOne.setName("Jules And Jim");
        ReferenceTag tagTwo = new ReferenceTag();
        tagTwo.setName("The 400 Blows");
        tagList.add(tagOne);
        tagList.add(tagTwo);
        groupSaveReqOne.setReferenceGroup(group);
        groupSaveReqOne.setReferenceTags(tagList);
        
        RefGroupSaveRequest groupSaveReqTwo = new RefGroupSaveRequest();        
        groupSaveReqTwo.setReferenceGroup(groups.iterator().next());
        groupSaveReqTwo.setReferenceTags(new ArrayList (groups.iterator().next().getTags()));
        
        refGroupSaveRequests.add(groupSaveReqOne);
        refGroupSaveRequests.add(groupSaveReqTwo);
        Set<ReferenceGroup> refreshedGroups = new HashSet();
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
        Set<ReferenceGroup> groups = new HashSet();
        for(RefGroupSaveRequest request: refGroupWithTags()){
            ReferenceGroup savedGroup = referenceGroupService.saveReferenceGroupAndReferenceTags(request);
            groups.add(savedGroup);
        }
        assertThat("Saved one group: ", groups.size(), is(1));

        ReferenceGroup one = groups.iterator().next();
        
        final ReferenceTag newTag = new ReferenceTag();
        newTag.setName("Weekend");

        List<RefGroupSaveRequest> refGroupSaveRequests = new ArrayList<>();
        List<ReferenceTag> tagList = new ArrayList<>();

        ReferenceGroupType groupType = new ReferenceGroupType();
        groupType.setName("REFERENCE DATA");
        groupType= referenceGroupTypeRepository.save(groupType);

        RefGroupSaveRequest groupSaveReqOne = new RefGroupSaveRequest();        
        ReferenceGroup group = new ReferenceGroup();
        group.setName("Francois Truffaut");
        group.setType(groupType);
        ReferenceTag tagOne = new ReferenceTag();
        tagOne.setName("Jules And Jim");
        ReferenceTag tagTwo = new ReferenceTag();
        tagTwo.setName("The 400 Blows");
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
        Set<ReferenceGroup> refreshedGroups = new HashSet();
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
}
