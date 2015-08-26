package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.ClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.persistence.ClientTeamEmailConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.DefaultClientTeamEmailConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.service.ClientTeamEmailConfigurationService;
import com.emmisolutions.emmimanager.service.TeamService;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

/**
 * Service layer for ClientTeamEmailConfiguration
 *
 */
@Service
public class ClientTeamEmailConfigurationServiceImpl implements ClientTeamEmailConfigurationService {

    private static final String EXCEPTION_NULL_TEAM_ID = "Team cannot be null to find ClientTeamEmailConfiguration";

    private static final String EXCEPTION_NULL_CLIENT_TEAM_EMAIL_CONFIGURATION = "ClientTeamEmailConfiguration or Team can not be null.";

    private static final String EXCEPTION_INVALID_TEAM = "Team does not exist.";

    @Resource
    TeamService teamService;

    @Resource
    TeamPersistence teamPersistence;

    @Resource
    DefaultClientTeamEmailConfigurationPersistence defaultClientTeamEmailConfigurationPersistence;

    @Resource
    ClientTeamEmailConfigurationPersistence clientTeamEmailConfigurationPersistence;

    @Override
    @Transactional
    public ClientTeamEmailConfiguration saveOrUpdate(ClientTeamEmailConfiguration clientTeamEmailConfiguration) {
        if (clientTeamEmailConfiguration == null || clientTeamEmailConfiguration.getTeam() == null) {
            throw new InvalidDataAccessApiUsageException(EXCEPTION_NULL_CLIENT_TEAM_EMAIL_CONFIGURATION);
        }

        Team reloadTeam = teamService.reload(clientTeamEmailConfiguration.getTeam());

        if (reloadTeam == null) {
            throw new InvalidDataAccessApiUsageException(EXCEPTION_INVALID_TEAM);
        }

        clientTeamEmailConfiguration.setTeam(reloadTeam);
        return clientTeamEmailConfigurationPersistence.save(clientTeamEmailConfiguration);
    }

    @Override
    public ClientTeamEmailConfiguration findByTeam(Team team) {
        if (team == null || team.getId() == null) {
            throw new InvalidDataAccessApiUsageException(EXCEPTION_NULL_TEAM_ID);
        }

        Team reloadTeam = teamPersistence.reload(team);

        if (reloadTeam == null) {
            throw new InvalidDataAccessApiUsageException(EXCEPTION_INVALID_TEAM);
        }

        ClientTeamEmailConfiguration clientTeamEmailConfiguration = clientTeamEmailConfigurationPersistence.find(team.getId());

        if (clientTeamEmailConfiguration == null) {
            clientTeamEmailConfiguration = getDefaultEmailConfiguration();
            clientTeamEmailConfiguration.setTeam(reloadTeam);
        }

        return clientTeamEmailConfiguration;
    }

    private ClientTeamEmailConfiguration getDefaultEmailConfiguration() {
        DefaultClientTeamEmailConfiguration defaultClientTeamEmailConfiguration = defaultClientTeamEmailConfigurationPersistence.find();
        ClientTeamEmailConfiguration clientTeamEmailConfiguration = new ClientTeamEmailConfiguration();

        clientTeamEmailConfiguration.setCollectEmail(defaultClientTeamEmailConfiguration.getCollectEmail());
        clientTeamEmailConfiguration.setRequireEmail(defaultClientTeamEmailConfiguration.getRequireEmail());
        clientTeamEmailConfiguration.setReminderTwoDays(defaultClientTeamEmailConfiguration.getReminderTwoDays());
        clientTeamEmailConfiguration.setReminderFourDays(defaultClientTeamEmailConfiguration.getReminderFourDays());
        clientTeamEmailConfiguration.setReminderSixDays(defaultClientTeamEmailConfiguration.getReminderSixDays());
        clientTeamEmailConfiguration.setReminderEightDays(defaultClientTeamEmailConfiguration.getReminderEightDays());
        clientTeamEmailConfiguration.setReminderArticles(defaultClientTeamEmailConfiguration.getReminderArticles());

        return clientTeamEmailConfiguration;
    }
}
