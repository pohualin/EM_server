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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import java.util.*;

/**
 * Service layer for ClientTeamEmailConfiguration
 *
 */
@Service
public class ClientTeamEmailConfigurationServiceImpl implements ClientTeamEmailConfigurationService {

    private static final String EXCEPTION_NULL_TEAM_ID = "Team cannot be null to find ClientTeamEmailConfiguration";

    private static final String EXCEPTION_NULL_CLIENT_TEAM_EMAIL_CONFIGURATION = "ClientTeamEmailConfiguration can not be null.";

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
        if (clientTeamEmailConfiguration == null) {
            throw new InvalidDataAccessApiUsageException(EXCEPTION_NULL_CLIENT_TEAM_EMAIL_CONFIGURATION);
        }

        Team reloadTeam = teamService.reload(clientTeamEmailConfiguration.getTeam());
        clientTeamEmailConfiguration.setTeam(reloadTeam);
        return clientTeamEmailConfigurationPersistence.save(clientTeamEmailConfiguration);
    }

    @Override
    public ClientTeamEmailConfiguration findByTeam(Team teamId) {
        if (teamId.getId() == null) {
            throw new InvalidDataAccessApiUsageException(EXCEPTION_NULL_TEAM_ID);
        }

        ClientTeamEmailConfiguration clientTeamEmailConfiguration = clientTeamEmailConfigurationPersistence.find(teamId.getId());

        // TODO: Refactor
        if (clientTeamEmailConfiguration == null) {
            Team reloadTeam = teamPersistence.reload(teamId);
            DefaultClientTeamEmailConfiguration defaultClientTeamEmailConfiguration = defaultClientTeamEmailConfigurationPersistence.find();
            ClientTeamEmailConfiguration newClientTeamEmailConfiguration = new ClientTeamEmailConfiguration();
            newClientTeamEmailConfiguration.setType(defaultClientTeamEmailConfiguration.getType()); // TODO: Remove
            newClientTeamEmailConfiguration.setRank(defaultClientTeamEmailConfiguration.getRank());
            newClientTeamEmailConfiguration.setEmailConfig(defaultClientTeamEmailConfiguration.isDefaultValue()); // TODO: Remove
            newClientTeamEmailConfiguration.setTeam(reloadTeam);
            newClientTeamEmailConfiguration.setCollectEmail(defaultClientTeamEmailConfiguration.getCollectEmail());
            newClientTeamEmailConfiguration.setRequireEmail(defaultClientTeamEmailConfiguration.getRequireEmail());
            newClientTeamEmailConfiguration.setReminderTwoDays(defaultClientTeamEmailConfiguration.getReminderTwoDays());
            newClientTeamEmailConfiguration.setReminderFourDays(defaultClientTeamEmailConfiguration.getReminderFourDays());
            newClientTeamEmailConfiguration.setReminderSixDays(defaultClientTeamEmailConfiguration.getReminderSixDays());
            newClientTeamEmailConfiguration.setReminderEightDays(defaultClientTeamEmailConfiguration.getReminderEightDays());
            newClientTeamEmailConfiguration.setReminderArticles(defaultClientTeamEmailConfiguration.getReminderArticles());
            return newClientTeamEmailConfiguration;
        } else {
            return clientTeamEmailConfiguration;
        }
    }

    @Override
    public Page<ClientTeamEmailConfiguration> findByTeam(Team team, Pageable pageable) {
        if (team.getId() == null) {
            throw new InvalidDataAccessApiUsageException(EXCEPTION_NULL_TEAM_ID);
        }

        Page<ClientTeamEmailConfiguration> teamEmailConfigDB = clientTeamEmailConfigurationPersistence.find(team.getId(), pageable);

        if (!teamEmailConfigDB.hasContent()) {
            Team reloadTeam = teamPersistence.reload(team);
            List<ClientTeamEmailConfiguration> clientTeamEmailConfigurationList = new ArrayList<ClientTeamEmailConfiguration> ();
            Page<DefaultClientTeamEmailConfiguration> deafaultClientEmail= defaultClientTeamEmailConfigurationPersistence.findActive(pageable);

            for (DefaultClientTeamEmailConfiguration defaultConfig : deafaultClientEmail) {
                ClientTeamEmailConfiguration teamEmailConfig = new ClientTeamEmailConfiguration();
                teamEmailConfig.setType(defaultConfig.getType()); // TODO: Remove
                teamEmailConfig.setRank(defaultConfig.getRank());
                teamEmailConfig.setEmailConfig(defaultConfig.isDefaultValue()); // TODO: Remove
                teamEmailConfig.setTeam(reloadTeam);
                teamEmailConfig.setCollectEmail(defaultConfig.getCollectEmail());
                teamEmailConfig.setRequireEmail(defaultConfig.getRequireEmail());
                teamEmailConfig.setReminderTwoDays(defaultConfig.getReminderTwoDays());
                teamEmailConfig.setReminderFourDays(defaultConfig.getReminderFourDays());
                teamEmailConfig.setReminderSixDays(defaultConfig.getReminderSixDays());
                teamEmailConfig.setReminderEightDays(defaultConfig.getReminderEightDays());
                teamEmailConfig.setReminderArticles(defaultConfig.getReminderArticles());
                clientTeamEmailConfigurationList.add(teamEmailConfig);
            }

            return new PageImpl<ClientTeamEmailConfiguration>(clientTeamEmailConfigurationList);
        } else {
            return teamEmailConfigDB;
        }
    }
}
