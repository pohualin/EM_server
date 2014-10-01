package com.emmisolutions.emmimanager.persistence.impl;

import static com.emmisolutions.emmimanager.persistence.impl.specification.TeamSpecifications.hasNames;
import static com.emmisolutions.emmimanager.persistence.impl.specification.TeamSpecifications.isInStatus;
import static com.emmisolutions.emmimanager.persistence.impl.specification.TeamSpecifications.usedBy;
import static org.springframework.data.jpa.domain.Specifications.where;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
import com.emmisolutions.emmimanager.persistence.repo.TeamRepository;

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
		team.setNormalizedTeamName(normalizeName(team));
		Team teams = teamRepository.save(team);
		return teams;		
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
     * @param name
     * @return
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
