package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.model.ReferenceGroupType;
import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ReferenceGroupPersistence;
import com.emmisolutions.emmimanager.persistence.ReferenceGroupTypePersistence;
import com.emmisolutions.emmimanager.persistence.ReferenceTagPersistence;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Reference Group integration tests
 */
public class ReferenceGroupPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    ReferenceGroupPersistence referenceGroupPersistence;
    
    @Resource
    ReferenceGroupTypePersistence referenceGroupTypePersistence;

    @Resource
    ReferenceTagPersistence referenceTagPersistence;

    @Test
    public void load(){
        assertThat("es is not present",
            referenceGroupPersistence.loadReferenceGroups(null).getTotalElements(),
            is(not(0l)));
    }
    
    @Test
    public void testGetAllTagsForGroup(){
        ReferenceGroupType groupType = new ReferenceGroupType();
        groupType.setName("reference data testing 3 2 1");
        referenceGroupTypePersistence.save(groupType);
        
        ReferenceGroupType findByName = referenceGroupTypePersistence.findByName("reference data testing 3 2 1");
        ReferenceGroup group = new ReferenceGroup();
        group.setName(RandomStringUtils.randomAlphanumeric(8));
        group.setType(findByName);
        ReferenceGroup savedGroup = referenceGroupPersistence.save(group);
        
        ReferenceGroup reloadGroup = referenceGroupPersistence.reload(savedGroup.getId());
        assertThat("reloaded group is not null: ", reloadGroup.getId(), is(notNullValue()));

        final ReferenceTag tagOne = new ReferenceTag();
        tagOne.setName(RandomStringUtils.randomAlphanumeric(8));
        tagOne.setGroup(reloadGroup);
        reloadGroup.getTags().add(tagOne);
        ReferenceTag t = referenceTagPersistence.save(tagOne);
        ReferenceTag reloadedTag = referenceTagPersistence.reload(t);
        assertThat("reloaded tag is not null: ", reloadedTag.getId(), is(notNullValue()));
        
        Pageable page = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        Page<ReferenceTag> tags = referenceTagPersistence.findAllByGroup(savedGroup, page);
        assertThat("Page of tags returned:", tags.getTotalElements(), is(1l));
        Page<ReferenceTag> tagsWithNoPageLimit = referenceTagPersistence.findAllByGroup(savedGroup, null);
        assertThat("Page of tags returned:", tagsWithNoPageLimit.getTotalElements(), is(1l));

        Page<ReferenceGroupType> types = referenceGroupTypePersistence.findAll(null);
        assertThat("types are present: ", types.getContent().size(), is(3));
        
        ReferenceGroup groupTwo = new ReferenceGroup();
        groupTwo.setName(RandomStringUtils.randomAlphanumeric(8));
        groupTwo.setType(findByName);
        ReferenceGroup savedGroupTwo = referenceGroupPersistence.save(groupTwo);
        assertThat("reloaded savedGroupTwo is not null: ", savedGroupTwo.getId(), is(notNullValue()));

        Page<ReferenceGroupType> typesTwo = referenceGroupTypePersistence.findAll(null);
        assertThat("types are present: ", typesTwo.getContent().size(), is(3));
    }
}
