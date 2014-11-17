package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.persistence.GroupPersistence;
import com.emmisolutions.emmimanager.persistence.repo.GroupRepository;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.emmisolutions.emmimanager.persistence.impl.specification.GroupSpecifications.byClientId;
import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * Group Persistence Implementation
 */
@Repository
public class GroupPersistenceImpl implements GroupPersistence {

    @Resource
    GroupRepository groupRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Group save(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public Page<Group> list(Pageable page, GroupSearchFilter searchFilter) {
        if (page == null) {
            // default pagination request if none
            page = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        }
        Page<Group> pageGroup = groupRepository.findAll(where(byClientId(searchFilter)), page);

        if (pageGroup.hasContent()) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            EntityGraph<Group> graph = entityManager.createEntityGraph(Group.class);
            graph.addAttributeNodes(Group_.tags);
            CriteriaQuery<Group> cq = cb.createQuery(Group.class);
            Root<Group> root = cq.from(Group.class);
            cq.select(root).where(root.in(pageGroup.getContent()));
            entityManager.createQuery(cq)
                    .setHint(QueryHints.LOADGRAPH, graph)
                    .getResultList();
        }
        return pageGroup;
    }

    @Override
    public Group reload(Long id) {
        if (id == null) {
            return null;
        }
        return groupRepository.findOne(id);
    }

    @Override
    public long removeGroupsThatAreNotAssociatedWith(Long clientId, Set<Long> groupIdsToKeep) {
        if (clientId != null) {
            if (groupIdsToKeep == null || groupIdsToKeep.isEmpty()) {
                return groupRepository.removeByClientIdEquals(clientId);
            } else {
                return groupRepository.removeByClientIdEqualsAndIdNotIn(clientId, groupIdsToKeep);
            }
        }
        return 0;
    }

    @Override
    public Set<TeamTag> findTeamsPreventingSaveOf(List<GroupSaveRequest> groupSaveRequests, Long clientId) {
        Set<Long> notInTheseTags = new HashSet<>();
        for (GroupSaveRequest groupSaveRequest : groupSaveRequests) {
            for (Tag tag : groupSaveRequest.getTags()) {
                if (tag.getId() != null) {
                    notInTheseTags.add(tag.getId());
                }
            }
        }
        List<TeamTag> conflictingTeams;
        if (notInTheseTags.size() == 0) {
            conflictingTeams = groupRepository.findTeamsPreventingSaveOf(clientId);
        } else {
            conflictingTeams = groupRepository.findTeamsPreventingSaveOf(clientId, notInTheseTags);
        }
        return new HashSet<>(conflictingTeams);
    }
}
