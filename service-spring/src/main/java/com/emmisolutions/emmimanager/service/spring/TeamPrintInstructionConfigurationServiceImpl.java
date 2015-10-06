package com.emmisolutions.emmimanager.service.spring;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.configuration.team.TeamPrintInstructionConfiguration;
import com.emmisolutions.emmimanager.persistence.DefaultTeamPrintInstructionConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPrintInstructionConfigurationPersistence;
import com.emmisolutions.emmimanager.service.TeamPrintInstructionConfigurationService;
import com.emmisolutions.emmimanager.service.TeamService;

/**
 * Service Implementation for TeamPrintInstructionConfiguration
 */
@Service
public class TeamPrintInstructionConfigurationServiceImpl implements
        TeamPrintInstructionConfigurationService {

    @Resource
    TeamService teamService;

    @Resource
    TeamPrintInstructionConfigurationPersistence teamPrintInstructionConfigurationPersistence;

    @Resource
    DefaultTeamPrintInstructionConfigurationPersistence defaultTeamPrintInstructionConfigurationPersistence;

    @Override
    @Transactional
    public void delete(
            TeamPrintInstructionConfiguration teamPrintInstructionConfiguration) {
        if (teamPrintInstructionConfiguration == null
                || teamPrintInstructionConfiguration.getId() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "TeamPrintInstructionConfiguration or teamPrintInstructionConfigurationId cannot be null");
        }
        teamPrintInstructionConfigurationPersistence
                .delete(teamPrintInstructionConfiguration.getId());
    }

    @Override
    @Transactional
    public TeamPrintInstructionConfiguration get(Team team) {
        Team toUse = teamService.reload(team);

        // Find TeamPrintInstructionConfiguration by Team
        TeamPrintInstructionConfiguration configuration = teamPrintInstructionConfigurationPersistence
                .findByTeam(toUse);

        // If nothing found, use system default
        if (configuration == null) {
            configuration = new TeamPrintInstructionConfiguration();
            configuration.setTeam(toUse);
            configuration
                    .setDefaultTeamPrintInstructionConfiguration(defaultTeamPrintInstructionConfigurationPersistence
                            .findActive());
            composeTeamPrintInstructionConfiguration(configuration);
        }

        return configuration;
    }

    @Override
    @Transactional
    public TeamPrintInstructionConfiguration reload(
            TeamPrintInstructionConfiguration teamPrintInstructionConfiguration) {
        if (teamPrintInstructionConfiguration == null
                || teamPrintInstructionConfiguration.getId() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "TeamPrintInstructionConfiguration or teamPrintInstructionConfigurationId can not be null");
        }
        return teamPrintInstructionConfigurationPersistence
                .reload(teamPrintInstructionConfiguration.getId());
    }

    @Override
    @Transactional
    public TeamPrintInstructionConfiguration create(
            TeamPrintInstructionConfiguration teamPrintInstructionConfiguration) {
        teamPrintInstructionConfiguration.setTeam(teamService
                .reload(teamPrintInstructionConfiguration.getTeam()));
        teamPrintInstructionConfiguration
                .setDefaultTeamPrintInstructionConfiguration(defaultTeamPrintInstructionConfigurationPersistence
                        .reload(teamPrintInstructionConfiguration
                                .getDefaultTeamPrintInstructionConfiguration()
                                .getId()));
        return teamPrintInstructionConfigurationPersistence
                .saveOrUpdate(teamPrintInstructionConfiguration);
    }

    @Override
    @Transactional
    public TeamPrintInstructionConfiguration update(
            TeamPrintInstructionConfiguration teamPrintInstructionConfiguration) {
        TeamPrintInstructionConfiguration inDb = reload(teamPrintInstructionConfiguration);
        if (inDb == null) {
            throw new InvalidDataAccessApiUsageException(
                    "Update method can only be used on existing teamPrintInstructionConfiguration");
        }

        teamPrintInstructionConfiguration.setTeam(inDb.getTeam());
        teamPrintInstructionConfiguration
                .setDefaultTeamPrintInstructionConfiguration(inDb
                        .getDefaultTeamPrintInstructionConfiguration());

        return teamPrintInstructionConfigurationPersistence
                .saveOrUpdate(teamPrintInstructionConfiguration);
    }

    private void composeTeamPrintInstructionConfiguration(
            TeamPrintInstructionConfiguration teamPrintInstructionConfiguration) {
        teamPrintInstructionConfiguration
                .setPatientUrl(teamPrintInstructionConfiguration
                        .getDefaultTeamPrintInstructionConfiguration()
                        .getPatientUrl());
    }

}
