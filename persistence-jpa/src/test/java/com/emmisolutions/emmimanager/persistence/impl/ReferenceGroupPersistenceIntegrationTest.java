package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.model.ReferenceGroupType;
import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ReferenceGroupPersistence;
import com.emmisolutions.emmimanager.persistence.ReferenceTagPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ReferenceGroupTypeRepository;

/**
 * Reference Group integration tests
 */
public class ReferenceGroupPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    ReferenceGroupPersistence referenceGroupPersistence;
    @Resource
    
    ReferenceTagPersistence referenceTagPersistence;
    
    @Resource
    ReferenceGroupTypeRepository referenceGroupTypeRepository;

    @Test
    public void load(){
        assertThat("es is not present",
            referenceGroupPersistence.loadReferenceGroups(null).getTotalElements(),
            is(not(0l)));
    }
    
    @Test
    public void testGetAllTagsForGroup(){
        ReferenceGroupType groupType = new ReferenceGroupType();
        groupType.setName(RandomStringUtils.randomAlphanumeric(8));
        groupType= referenceGroupTypeRepository.save(groupType);
        ReferenceGroup group = new ReferenceGroup();
        group.setName(RandomStringUtils.randomAlphanumeric(8));
        group.setType(groupType);
        ReferenceGroup savedGroup = referenceGroupPersistence.save(group);

        final ReferenceTag tagOne = new ReferenceTag();
        tagOne.setName(RandomStringUtils.randomAlphanumeric(8));
        tagOne.setGroup(group);
        ReferenceTag t = referenceTagPersistence.save(tagOne);
        ReferenceTag reloadedTag = referenceTagPersistence.reload(t);
        assertThat("reloaded tag is not null: ", reloadedTag.getId(), is(notNullValue()));
        
        Pageable page = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        Page<ReferenceTag> tags = referenceTagPersistence.findAllByGroup(savedGroup, page);
        assertThat("Page of tags returned:", tags.getTotalElements(), is(1l));
        Page<ReferenceTag> tagsWithNoPageLimit = referenceTagPersistence.findAllByGroup(savedGroup, null);
        assertThat("Page of tags returned:", tagsWithNoPageLimit.getTotalElements(), is(1l));

    }
}
