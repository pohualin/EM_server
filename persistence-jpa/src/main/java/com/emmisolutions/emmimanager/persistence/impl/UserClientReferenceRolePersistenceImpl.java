package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.user.client.reference.UserClientReferenceRole;
import com.emmisolutions.emmimanager.model.user.client.reference.UserClientReferenceRoleType;
import com.emmisolutions.emmimanager.model.user.client.reference.UserClientReferenceRole_;
import com.emmisolutions.emmimanager.persistence.UserClientReferenceRolePersistence;
import com.emmisolutions.emmimanager.persistence.repo.UserClientReferenceRoleRepository;
import com.emmisolutions.emmimanager.persistence.repo.UserClientReferenceRoleTypeRepository;
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
public class UserClientReferenceRolePersistenceImpl implements UserClientReferenceRolePersistence {

	@Resource
    UserClientReferenceRoleRepository userClientReferenceRoleRepository;

    @Resource
    UserClientReferenceRoleTypeRepository userClientReferenceRoleTypeRepository;

    @PersistenceContext
	EntityManager entityManager;

	@Override
	public Page<UserClientReferenceRole> loadReferenceRoles(Pageable page) {
		Pageable pageSpecification;
        if (page == null) {
			// default pagination request if none
            pageSpecification = new PageRequest(0, 50, Sort.Direction.ASC, "id");
		} else {
            pageSpecification = page;
        }
		Page<UserClientReferenceRole> roles = userClientReferenceRoleRepository.findAll(pageSpecification);

        // eagerly fetch permissions for the page of roles
		if (roles.hasContent()) {
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			EntityGraph<UserClientReferenceRole> graph = entityManager.createEntityGraph(UserClientReferenceRole.class);
			graph.addAttributeNodes(UserClientReferenceRole_.permissions);
			CriteriaQuery<UserClientReferenceRole> cq = cb.createQuery(UserClientReferenceRole.class);
			Root<UserClientReferenceRole> root = cq.from(UserClientReferenceRole.class);
			cq.select(root).where(root.in(roles.getContent()));
			entityManager.createQuery(cq).setHint(QueryHints.LOADGRAPH, graph).getResultList();
		}
		return roles;
	}

    @Override
    public UserClientReferenceRoleType reload(UserClientReferenceRoleType type) {
        if (type == null || type.getId() == null){
            return null;
        }
        return userClientReferenceRoleTypeRepository.findOne(type.getId());
    }
}
