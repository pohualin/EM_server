package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.configuration.team.TeamPrintInstructionConfiguration;

/**
 * TeamPrintInstructionConfiguration Persistence
 */
public interface TeamPrintInstructionConfigurationPersistence {

    /**
     * Delete existing TeamPrintInstructionConfiguration by id
     * 
     * @param id
     *            to delete
     */
    public void delete(Long id);

    /**
     * Find TeamPrintInstructionConfiguration by Team
     * 
     * @param team
     *            to use
     * @return null or an existing TeamPrintInstructionConfiguration
     */
    public TeamPrintInstructionConfiguration findByTeam(Team team);

    /**
     * Reload TeamPrintInstructionConfiguration by passed in id
     * 
     * @param id
     *            to reload
     * @return an existing TeamPrintInstructionConfiguration
     */
    public TeamPrintInstructionConfiguration reload(Long id);

    /**
     * Save or update the passed in TeamPrintInstructionConfiguration
     * 
     * @param teamPrintInstructionConfiguration
     *            to save or update
     * @return the saved or updated TeamPrintInstructionConfiguration
     */
    public TeamPrintInstructionConfiguration saveOrUpdate(
            TeamPrintInstructionConfiguration teamPrintInstructionConfiguration);

}
