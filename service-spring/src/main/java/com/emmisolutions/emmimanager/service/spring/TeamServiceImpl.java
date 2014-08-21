package com.emmisolutions.emmimanager.service.spring;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamSearchFilter;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.service.TeamService;

/**
 * Implementation of the TeamService
 */
@Service
public class TeamServiceImpl implements TeamService {
	
	@Resource
    TeamPersistence teamPersistence;
	
	/**
	 * @param teamSerchFilter  search filter for teams
	 * @return a page of teams
	 */
	@Override
    @Transactional(readOnly = true)
    public Page<Team> list(TeamSearchFilter teamSearchFilter) {
        return list(null, teamSearchFilter);
    }

	/**
	 * @param page  Page number of teams needed 
	 * @param teamSerchFilter  search filter for teams
	 * @return a particular page of teams 
	 */
    @Override
    @Transactional(readOnly = true)
    public Page<Team> list(Pageable page, TeamSearchFilter teamSearchFilter) {
        return teamPersistence.list(page, teamSearchFilter);
    }
    
    /**
     * @param toFind  the team that needs to be retreived
     * @return Team   loads a team from the database
     */
    @Override
    public Team reload(Team toFind) {
        return teamPersistence.reload(toFind);
    }

    /**
     * @param team   Team to be saved in the database
     * @return Team  returns a persisted Team entity	
     */
	@Override
	public Team create(Team team) {
		if (team == null) {
            throw new IllegalArgumentException("team cannot be null");
        }
        team.setId(null);
        team.setVersion(null);
        return teamPersistence.save(team);
	}
}
