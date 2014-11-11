package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamSearchFilter;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
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

@Repository
public class TeamPersistenceImpl implements TeamPersistence {

	@Resource
    TeamRepository teamRepository;

	@Resource
    TeamSpecifications teamSpecifications;

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
        return teamRepository.findAll(where(teamSpecifications.usedBy(client))
            .and(teamSpecifications.hasNames(filter)).and(teamSpecifications.isInStatus(filter)), page);
	}

	@Override
	public Team save(Team team) {
		team.setNormalizedTeamName(normalizeName(team));
		return teamRepository.save(team);
	}

	@Override
	public Team reload(Team team) {
		if (team == null || team.getId() == null) {
            return null;
        }
        return teamRepository.findOne(team.getId());
	}

    /**
     * remove the special characters replacing it with blank (" ") and change all to lower case
     *
     * @param name the name
     * @return normalized name
     */
    private String normalizeName(String name) {
    	String normalizedName = StringUtils.trimToEmpty(StringUtils.lowerCase(name));
    	if (StringUtils.isNotBlank(normalizedName)){
    	    // do regex
    	    normalizedName = normalizedName.replaceAll("[^a-z0-9]*","");
    	}
    	return normalizedName;
    }

    private String normalizeName(Team team){
    	return normalizeName(team.getName()==null?"":team.getName());
    }

    @Override
	public Team findByNormalizedTeamNameAndClientId(String normalizedName, Long clientId) {
		String toSearchTeamNameBy = normalizeName(normalizedName);
		Team team = null;
		if (StringUtils.isNotBlank(toSearchTeamNameBy)) {
			team = teamRepository.findByNormalizedTeamNameAndClientId(toSearchTeamNameBy, clientId);
		}
		return team;
    }
}
