package com.emmisolutions.emmimanager.persistence.impl;

import static com.emmisolutions.emmimanager.persistence.impl.specification.GroupSpecifications.byClientId;
import static org.springframework.data.jpa.domain.Specifications.where;

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
import com.emmisolutions.emmimanager.persistence.GroupPersistence;
import com.emmisolutions.emmimanager.persistence.repo.GroupRepository;

/**
 * Group Persistence Implementation
 *
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
        if (id == null) {
            return null;
        }
		return groupRepository.findOne(id);
	}

    @Override
    public Long removeAll(Long clientId) {
        if (clientId != null){
            Long ret = groupRepository.removeByClientIdEquals(clientId);
            // need to commit deletes immediately
            groupRepository.flush();
            return ret;
        }
        return 0l;
    }
}
