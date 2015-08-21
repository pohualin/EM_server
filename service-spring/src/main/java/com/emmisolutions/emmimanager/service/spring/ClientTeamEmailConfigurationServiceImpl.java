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
            throw new InvalidDataAccessApiUsageException("ClientTeamEmailConfiguration can not be null.");
        }

        Team reloadTeam = teamService.reload(clientTeamEmailConfiguration.getTeam());
        clientTeamEmailConfiguration.setTeam(reloadTeam);
        return clientTeamEmailConfigurationPersistence.save(clientTeamEmailConfiguration);
    }

    @Override
    public Page<ClientTeamEmailConfiguration> findByTeam(Team team, Pageable pageable) {
        if (team.getId() == null) {
            throw new InvalidDataAccessApiUsageException("Team cannot be null to find ClientTeamEmailConfiguration");
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
