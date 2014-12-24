package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamSearchFilter;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.persistence.impl.specification.MatchingCriteriaBean;
import com.emmisolutions.emmimanager.persistence.impl.specification.TeamSpecifications;
import com.emmisolutions.emmimanager.persistence.repo.TeamRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import static org.springframework.data.jpa.domain.Specifications.where;

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
        return teamRepository.findAll(where(teamSpecifications.usedByClient(filter))
                        .and(teamSpecifications.hasNames(filter))
                        .and(teamSpecifications.isInStatus(filter)),
                page == null ? new PageRequest(0, 50, Sort.Direction.ASC, "id") : page);
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
