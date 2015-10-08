package com.emmisolutions.emmimanager.persistence.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.configuration.team.TeamPrintInstructionConfiguration;
import com.emmisolutions.emmimanager.persistence.TeamPrintInstructionConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.repo.TeamPrintInstructionConfigurationRepository;

/**
 * Persistence Implementation to deal with TeamPrintInstructionConfiguration
 */
@Repository
public class TeamPrintInstructionConfigurationPersistenceImpl implements
        TeamPrintInstructionConfigurationPersistence {

    @Resource
    TeamPrintInstructionConfigurationRepository teamPrintInstructionConfigurationRepository;

    @Override
    public TeamPrintInstructionConfiguration findByTeam(Team team) {
        if (team == null || team.getId() == null) {
            return null;
        }
        return teamPrintInstructionConfigurationRepository.findByTeam(team);
    }

    @Override
    public TeamPrintInstructionConfiguration reload(Long id) {
        return teamPrintInstructionConfigurationRepository.findOne(id);
    }

    @Override
    public TeamPrintInstructionConfiguration saveOrUpdate(
            TeamPrintInstructionConfiguration teamPrintInstructionConfiguration) {
        return teamPrintInstructionConfigurationRepository
                .save(teamPrintInstructionConfiguration);
    }

    @Override
    public void delete(Long id) {
        TeamPrintInstructionConfiguration toDelete = reload(id);
        if (toDelete != null) {
            teamPrintInstructionConfigurationRepository.delete(toDelete);
        }
    }

}
