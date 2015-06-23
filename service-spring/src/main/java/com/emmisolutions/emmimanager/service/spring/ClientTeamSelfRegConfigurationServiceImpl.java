package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.ClientTeamSelfRegConfiguration;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.persistence.ClientTeamSelfRegConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.DefaultClientTeamPhoneConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.service.ClientTeamSelfRegConfigurationService;
import com.emmisolutions.emmimanager.service.TeamService;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

/**
 * Service layer for ClientTeamSelfRegConfiguration
 */
@Service
public class ClientTeamSelfRegConfigurationServiceImpl implements
        ClientTeamSelfRegConfigurationService {

    @Resource
    TeamService teamService;

    @Resource
    TeamPersistence teamPersistence;

    @Resource
    DefaultClientTeamPhoneConfigurationPersistence defaultClientTeamPhoneConfigurationPersistence;

    @Resource
    ClientTeamSelfRegConfigurationPersistence clientTeamSelfRegConfigurationPersistence;

    @Override
    @Transactional
    public ClientTeamSelfRegConfiguration create(
            ClientTeamSelfRegConfiguration clientTeamSelfRegConfiguration) {
        if (clientTeamSelfRegConfiguration == null) {
            throw new InvalidDataAccessApiUsageException(
                    "ClientTeamSelfRegConfiguration can not be null.");
        }
        if (!isCodeAlreadyUsed(clientTeamSelfRegConfiguration)) {
            ClientTeamSelfRegConfiguration clientTeamSelfRegConfigurationReloaded = new ClientTeamSelfRegConfiguration();
            clientTeamSelfRegConfigurationReloaded.setTeam(teamService.reload(clientTeamSelfRegConfiguration.getTeam()));
            clientTeamSelfRegConfigurationReloaded.setCode(clientTeamSelfRegConfiguration.getCode());
            return clientTeamSelfRegConfigurationPersistence.save(clientTeamSelfRegConfigurationReloaded);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public ClientTeamSelfRegConfiguration update(
            ClientTeamSelfRegConfiguration clientTeamSelfRegConfiguration) {
        if (clientTeamSelfRegConfiguration == null || clientTeamSelfRegConfiguration.getId() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "ClientTeamSelfRegConfiguration can not be null.");
        }
        if (!isCodeAlreadyUsed(clientTeamSelfRegConfiguration)) {
            ClientTeamSelfRegConfiguration clientTeamSelfRegConfigurationReloaded;
            clientTeamSelfRegConfigurationReloaded = clientTeamSelfRegConfigurationPersistence.reload(clientTeamSelfRegConfiguration.getId());
            clientTeamSelfRegConfigurationReloaded.setCode(clientTeamSelfRegConfiguration.getCode());
            return clientTeamSelfRegConfigurationPersistence.save(clientTeamSelfRegConfigurationReloaded);
        } else {
            return null;
        }
    }

    private boolean isCodeAlreadyUsed(ClientTeamSelfRegConfiguration clientTeamSelfRegConfiguration) {
        ClientTeamSelfRegConfiguration config = clientTeamSelfRegConfigurationPersistence.findByCode(clientTeamSelfRegConfiguration.getCode());
        if (config != null && (clientTeamSelfRegConfiguration.getId() != null && config.getId() != clientTeamSelfRegConfiguration.getId())) {
            return true;
        }
        return false;
    }

    @Override
    public ClientTeamSelfRegConfiguration findByTeam(Team team) {
        if (team == null || team.getId() == null) {
            throw new InvalidDataAccessApiUsageException("Team cannot be null" + "to find ClientTeamSelfRegConfiguration");
        }
        return clientTeamSelfRegConfigurationPersistence.find(team.getId());
    }
}
