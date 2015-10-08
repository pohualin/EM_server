package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.configuration.team.TeamPrintInstructionConfiguration;

/**
 * The TeamPrintInstructionConfiguration Service
 */
public interface TeamPrintInstructionConfigurationService {

    /**
     * Delete the existing TeamPrintInstructionConfiguration
     */
    public void delete(
            TeamPrintInstructionConfiguration teamPrintInstructionConfiguration);

    /**
     * Get the TeamPrintInstructionConfiguration for a Team
     * 
     * @param team
     *            to lookup
     * @return an existing TeamPrintInstructionConfiguration or compose one from
     *         DefaultTeamPrintInstructionConfiguration
     */
    public TeamPrintInstructionConfiguration get(Team team);

    /**
     * Reload TeamPrintInstructionConfiguration by id
     * 
     * @param teamPrintInstructionConfiguration
     *            to reload
     * @return an existing TeamPrintInstructionConfiguration
     */
    public TeamPrintInstructionConfiguration reload(
            TeamPrintInstructionConfiguration teamPrintInstructionConfiguration);

    /**
     * Create a TeamPrintInstructionConfiguration
     * 
     * @param teamPrintInstructionConfiguration
     *            to create
     * @return created TeamPrintInstructionConfiguration
     */
    public TeamPrintInstructionConfiguration create(
            TeamPrintInstructionConfiguration teamPrintInstructionConfiguration);

    /**
     * Update an existing TeamPrintInstructionConfiguration
     * 
     * @param teamPrintInstructionConfiguration
     *            to update
     * @return updated TeamPrintInstructionConfiguration
     */
    public TeamPrintInstructionConfiguration update(
            TeamPrintInstructionConfiguration teamPrintInstructionConfiguration);
}
