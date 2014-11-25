package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.model.TagSearchFilter;
import com.emmisolutions.emmimanager.model.TeamTag;
import com.emmisolutions.emmimanager.persistence.TagPersistence;
import com.emmisolutions.emmimanager.persistence.repo.TagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.emmisolutions.emmimanager.persistence.impl.specification.TagSpecifications.byGroupId;
import static org.springframework.data.jpa.domain.Specifications.where;

@Repository
public class TagPersistenceImpl implements TagPersistence {

    @Resource
    TagRepository tagRepository;

    @Override
    public Page<Tag> listTagsByGroupId(Pageable page, TagSearchFilter searchFilter) {
        if (page == null) {
            // default pagination request if none
            page = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        }
        return tagRepository.findAll(where(byGroupId(searchFilter)), page);
    }

    @Override
    public Tag save(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public Tag reload(Tag tag) {
        if (tag == null) {
            return null;
        }
        return tagRepository.findOne(tag.getId());
    }

    @Override
    public List<Tag> createAll(List<Tag> createTagsList) {
        return tagRepository.save(createTagsList);
    }

    @Override
    public long removeTagsThatAreNotAssociatedWith(Long clientId, Set<Long> groupIdsToKeep) {
        if (clientId != null) {
            if (groupIdsToKeep == null || groupIdsToKeep.isEmpty()){
                return tagRepository.deleteByGroupClientIdEquals(clientId);
            } else {
                return tagRepository.deleteByGroupClientIdEqualsAndGroupIdNotIn(clientId, groupIdsToKeep);
            }
        }
        return 0;
    }

    @Override
    public Set<TeamTag> findTeamsWithTagId(Long tagId) {
        return new HashSet<>(tagRepository.findTeamsWithTagId(tagId));
    }

}
