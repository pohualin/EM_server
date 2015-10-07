package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamSalesForce;
import com.emmisolutions.emmimanager.model.TeamSearchFilter;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.service.SalesForceService;
import com.emmisolutions.emmimanager.service.TeamService;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Implementation of the TeamService
 */
@Service
public class TeamServiceImpl implements TeamService {

    @Resource
    TeamPersistence teamPersistence;

    @Resource
    ClientPersistence clientPersistence;

    @Resource
    SalesForceService salesForceService;

    /**
     * @param teamSearchFilter search filter for teams
     * @return a page of teams
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Team> list(TeamSearchFilter teamSearchFilter) {
        return list(null, teamSearchFilter);
    }

    /**
     * @param page             Page number of teams needed
     * @param teamSearchFilter search filter for teams
     * @return a particular page of teams
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Team> list(Pageable page, TeamSearchFilter teamSearchFilter) {
        return teamPersistence.list(page, teamSearchFilter);
    }

    /**
     * @param toFind the team that needs to be retreived
     * @return Team   loads a team from the database
     */
    @Override
    public Team reload(Team toFind) {
        if (toFind == null) {
            throw new InvalidDataAccessApiUsageException("Team cannot be null");
        }
        return teamPersistence.reload(toFind);
    }

    /**
     * @param team Team to be saved in the database
     * @return Team  returns a persisted Team entity
     */
    @Override
    @Transactional
    public Team create(Team team) {
        if (team == null) {
            throw new IllegalArgumentException("teams cannot be null");
        }
        team.setId(null);
        team.setVersion(null);

        if (team.getSalesForceAccount() != null && team.getSalesForceAccount().getAccountNumber() != null) {
            TeamSalesForce tsf = new TeamSalesForce();
            tsf.setAccountNumber(team.getSalesForceAccount().getAccountNumber());
            team.setSalesForceAccount(updateSalesforceDetails(tsf));
        }
        return teamPersistence.save(team);
    }

    @Override
    public Team findByNormalizedNameAndClientId(String normalizedName, Long clientId) {
        return teamPersistence.findByNormalizedTeamNameAndClientId(normalizedName, clientId);
    }

    @Override
    @Transactional
    public Team update(Team team) {
        Team dbTeam = teamPersistence.reload(team);
        if (dbTeam == null) {
            throw new IllegalArgumentException("Client Id, Team Id and Version cannot be null.");
        }
        // don't allow client changes on updates
        team.setClient(dbTeam.getClient());
        TeamSalesForce tsf = dbTeam.getSalesForceAccount();
        tsf.setAccountNumber(team.getSalesForceAccount().getAccountNumber());
        team.setSalesForceAccount(updateSalesforceDetails(tsf));
        return teamPersistence.save(team);
    }

    private TeamSalesForce updateSalesforceDetails(TeamSalesForce tsf) {
        SalesForce sf = salesForceService.findAccountById(tsf.getAccountNumber());
        if (sf != null) {
            tsf.setCity(sf.getCity());
            tsf.setCountry(sf.getCountry());
            tsf.setFaxNumber(sf.getFax());
            tsf.setName(sf.getName());
            tsf.setPhoneNumber(sf.getPhoneNumber());
            tsf.setPostalCode(sf.getPostalCode());
            tsf.setState(sf.getState());
            tsf.setStreet(sf.getStreet());
        }
        return tsf;
    }
}
