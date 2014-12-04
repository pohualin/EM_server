package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.user.client.team.reference.UserClientReferenceTeamRole;
import com.emmisolutions.emmimanager.model.user.client.team.reference.UserClientReferenceTeamRoleType;
import com.emmisolutions.emmimanager.model.user.client.team.reference.UserClientReferenceTeamRole_;
import com.emmisolutions.emmimanager.persistence.UserClientReferenceTeamRolePersistence;
import com.emmisolutions.emmimanager.persistence.repo.UserClientReferenceTeamRoleRepository;
import com.emmisolutions.emmimanager.persistence.repo.UserClientReferenceTeamRoleTypeRepository;
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

/**
 *  UserClientReferenceTeamRolePersistence implementation
 */
@Repository
public class UserClientReferenceTeamRolePersistenceImpl implements UserClientReferenceTeamRolePersistence {

	@Resource
    UserClientReferenceTeamRoleRepository userClientReferenceTeamRoleRepository;

    @Resource
    UserClientReferenceTeamRoleTypeRepository userClientReferenceTeamRoleTypeRepository;

    @PersistenceContext
	EntityManager entityManager;

	@Override
	public Page<UserClientReferenceTeamRole> loadReferenceTeamRoles(Pageable page) {
		Pageable pageSpecification;
        if (page == null) {
			// default pagination request if none
            pageSpecification = new PageRequest(0, 50, Sort.Direction.ASC, "id");
		} else {
            pageSpecification = page;
        }
		Page<UserClientReferenceTeamRole> roles = userClientReferenceTeamRoleRepository.findAll(pageSpecification);

        // eagerly fetch permissions for the page of roles
		if (roles.hasContent()) {
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			EntityGraph<UserClientReferenceTeamRole> graph = entityManager.createEntityGraph(UserClientReferenceTeamRole.class);
			graph.addAttributeNodes(UserClientReferenceTeamRole_.permissions);
			CriteriaQuery<UserClientReferenceTeamRole> cq = cb.createQuery(UserClientReferenceTeamRole.class);
			Root<UserClientReferenceTeamRole> root = cq.from(UserClientReferenceTeamRole.class);
			cq.select(root).where(root.in(roles.getContent()));
			entityManager.createQuery(cq).setHint(QueryHints.LOADGRAPH, graph).getResultList();
		}
		return roles;
	}

    @Override
    public UserClientReferenceTeamRoleType reload(UserClientReferenceTeamRoleType type) {
        if (type == null || type.getId() == null){
            return null;
        }
        return userClientReferenceTeamRoleTypeRepository.findOne(type.getId());
    }
}
