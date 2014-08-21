package com.emmisolutions.emmimanager.persistence.impl;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamSearchFilter;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.persistence.repo.TeamRepository;

import static com.emmisolutions.emmimanager.persistence.impl.specification.TeamSpecifications.*;
import static org.springframework.data.jpa.domain.Specifications.where;

@Repository
public class TeamPersistenceImpl implements TeamPersistence {
	
	@Resource
    TeamRepository teamRepository;
	
	@PersistenceContext
    EntityManager entityManager;

    @Resource
    ClientPersistence clientPersistence;	

	@Override
	public Page<Team> list(Pageable page, TeamSearchFilter filter) {
		if (page == null) {
            // default pagination request if none
            page = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        }
		Client client = null;
        if (filter != null && filter.getClientId() != null){
            client = clientPersistence.reload(filter.getClientId());
        }
        Page<Team> ret = teamRepository.findAll(where(usedBy(client)).and(hasNames(filter)).and(isInStatus(filter)), page);

        return ret;
	}

	@Override
	public Team save(Team team) {
		return teamRepository.save(team);
	}

	@Override
	public Team reload(Team team) {
		if (team == null || team.getId() == null) {
            return null;
        }
        return teamRepository.findOne(team.getId());
	}

}
