package com.emmisolutions.emmimanager.persistence.impl;

import static com.emmisolutions.emmimanager.persistence.impl.specification.GroupSpecifications.byClientId;
import static org.springframework.data.jpa.domain.Specifications.where;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.Group;
import com.emmisolutions.emmimanager.model.GroupSearchFilter;
import com.emmisolutions.emmimanager.model.Group_;
import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.persistence.GroupPersistence;
import com.emmisolutions.emmimanager.persistence.repo.GroupRepository;
import com.emmisolutions.emmimanager.persistence.repo.ReferenceGroupRepository;

/**
 * Group Persistence Implementation
 *
 */
@Repository
public class GroupPersistenceImpl implements GroupPersistence {

	@Resource
	GroupRepository groupRepository;

	@Resource
	ReferenceGroupRepository referenceGroupRepository;

    @PersistenceContext
    EntityManager entityManager;

	@Override
	public Collection<ReferenceGroup> fetchReferenceGroups() {
		return referenceGroupRepository.fetchReferenceGroups();
	}

	@Override
	public Group save(Group group) {
		return groupRepository.save(group);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<Group> list(Pageable page, GroupSearchFilter searchFilter) {
		if (page == null) {
			// default pagination request if none
			page = new PageRequest(0, 50, Sort.Direction.ASC, "id");
		}
		Page<Group> pageGroup= groupRepository.findAll(where(byClientId(searchFilter)), page);
		
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
		return groupRepository.findOne(id);
	}

	@Override
	public void remove(Long id) {
		groupRepository.delete(id);
	}
	
	@Override
	public List<Group> updateAll(List<Group> editGroupsList) {
		return groupRepository.save(editGroupsList);
	}

	@Override
	public void removeAll(List<Group> removeGroupsList) {
		groupRepository.delete(removeGroupsList);
	}

	@Override
	public List<Group> createAll(List<Group> createGroupsList) {
		return groupRepository.save(createGroupsList);
	}

}
