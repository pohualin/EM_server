package com.emmisolutions.emmimanager.persistence.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.configuration.PasswordConfiguration;
import com.emmisolutions.emmimanager.persistence.PasswordConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.repo.PasswordConfigurationRepository;

/**
 * Persistence Implementation to deal with PasswordConfiguration
 */
@Repository
public class PasswordConfigurationPersistenceImpl implements
        PasswordConfigurationPersistence {

    @Resource
    PasswordConfigurationRepository passwordConfigurationRepository;

    @Override
    public PasswordConfiguration reload(Long id) {
        return passwordConfigurationRepository.findOne(id);
    }

    @Override
    public PasswordConfiguration saveOrUpdate(
            PasswordConfiguration passwordConfiguration) {
        return passwordConfigurationRepository.save(passwordConfiguration);
    }

}
