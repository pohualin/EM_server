package com.emmisolutions.emmimanager.persistence.impl;

import static org.springframework.data.jpa.domain.Specifications.where;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamSearchFilter;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.persistence.impl.specification.MatchingCriteriaBean;
import com.emmisolutions.emmimanager.persistence.impl.specification.TeamSpecifications;
import com.emmisolutions.emmimanager.persistence.repo.TeamRepository;

/**
 * Persistence implementation for TeamPersistence
 */
@Repository
public class TeamPersistenceImpl implements TeamPersistence {

	@Resource
    TeamRepository teamRepository;

	@Resource
    TeamSpecifications teamSpecifications;

    @Resource
    ClientPersistence clientPersistence;
    
    @Resource
    MatchingCriteriaBean matchCriteria;
    
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
        return teamRepository.findAll(where(teamSpecifications.usedBy(client))
            .and(teamSpecifications.hasNames(filter)).and(teamSpecifications.isInStatus(filter)), page);
	}

	@Override
	public Team save(Team team) {
		team.setNormalizedTeamName(matchCriteria.normalizeNameAndBlank(team.getName()));
		return teamRepository.save(team);
	}

	@Override
	public Team reload(Team team) {
		if (team == null || team.getId() == null) {
            return null;
        }
        return teamRepository.findOne(team.getId());
	}

    @Override
	public Team findByNormalizedTeamNameAndClientId(String normalizedName, Long clientId) {
		String toSearchTeamNameBy = matchCriteria.normalizeNameAndBlank(normalizedName);
		Team team = null;
		if (StringUtils.isNotBlank(toSearchTeamNameBy)) {
			team = teamRepository.findByNormalizedTeamNameAndClientId(toSearchTeamNameBy, clientId);
		}
		return team;
    }
}
