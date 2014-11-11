package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.model.ReferenceGroup_;
import com.emmisolutions.emmimanager.persistence.ReferenceGroupPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ReferenceGroupRepository;
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

@Repository
public class ReferenceGroupPersistenceImpl implements ReferenceGroupPersistence {

	@Resource
	ReferenceGroupRepository referenceGroupRepository;

    @PersistenceContext
	EntityManager entityManager;

	@Override
	public Page<ReferenceGroup> loadReferenceGroups(Pageable page) {
		if (page == null) {
			// default pagination request if none
			page = new PageRequest(0, 50, Sort.Direction.ASC, "id");
		}
		Page<ReferenceGroup> groups = referenceGroupRepository.findAll(page);

		if (groups.hasContent()) {
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			EntityGraph<ReferenceGroup> graph = entityManager.createEntityGraph(ReferenceGroup.class);
			graph.addAttributeNodes(ReferenceGroup_.tags);
			CriteriaQuery<ReferenceGroup> cq = cb.createQuery(ReferenceGroup.class);
			Root<ReferenceGroup> root = cq.from(ReferenceGroup.class);
			cq.select(root).where(root.in(groups.getContent()));
			entityManager.createQuery(cq).setHint(QueryHints.LOADGRAPH, graph).getResultList();
		}
		return groups;
	}
}
