package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.configuration.team.DefaultTeamPrintInstructionConfiguration;

/**
 * Default Team Print Instruction Configuration Persistence
 */
public interface DefaultTeamPrintInstructionConfigurationPersistence {

    /**
     * Find active DefaultTeamPrintInstructionConfiguration
     * 
     * @return active DefaultTeamPrintInstructionConfiguration
     */
    public DefaultTeamPrintInstructionConfiguration findActive();

    /**
     * Reload DefaultTeamPrintInstructionConfiguration by passed in id
     * 
     * @param id
     *            to reload
     * @return an existing DefaultTeamPrintInstructionConfiguration
     */
    public DefaultTeamPrintInstructionConfiguration reload(Long id);

    /**
     * Save or update the passed in DefaultTeamPrintInstructionConfiguration
     * 
     * @param defaultTeamPrintInstructionConfiguration
     *            to save or update
     * @return the saved or updated DefaultTeamPrintInstructionConfiguration
     */
    public DefaultTeamPrintInstructionConfiguration saveOrUpdate(
            DefaultTeamPrintInstructionConfiguration defaultTeamPrintInstructionConfiguration);

}
