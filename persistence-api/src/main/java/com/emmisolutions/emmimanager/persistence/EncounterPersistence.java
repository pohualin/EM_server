package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.schedule.Encounter;

/**
 * Persistence Layer for Encounter
 */
public interface EncounterPersistence {

    /**
     * Saves an encounter
     *
     * @param toSave to save
     * @return the saved Encounter
     */
    Encounter save(Encounter toSave);

    /**
     * Loads the encounter
     *
     * @param encounter to load
     * @return the reloaded Encounter
     */
    Encounter reload(Encounter encounter);

}
