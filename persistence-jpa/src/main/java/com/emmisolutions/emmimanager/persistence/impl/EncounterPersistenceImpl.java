package com.emmisolutions.emmimanager.persistence.impl;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.schedule.Encounter;
import com.emmisolutions.emmimanager.persistence.EncounterPersistence;
import com.emmisolutions.emmimanager.persistence.repo.EncounterRepository;

/**
 * Schedule persistence repository
 */
@Repository
public class EncounterPersistenceImpl implements EncounterPersistence {

    @Resource
    EncounterRepository encounterRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Encounter save(Encounter toSave) {
        return encounterRepository.save(toSave);
    }

    @Override
    public Encounter reload(final Encounter encounter) {
        if (encounter == null || encounter.getId() == null) {
            return null;
        }
        return encounterRepository.findOne(encounter.getId());
    }

}
