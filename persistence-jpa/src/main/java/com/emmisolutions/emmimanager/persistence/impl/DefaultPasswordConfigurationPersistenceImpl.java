package com.emmisolutions.emmimanager.persistence.impl;

import static org.springframework.data.jpa.domain.Specifications.where;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.configuration.DefaultPasswordConfiguration;
import com.emmisolutions.emmimanager.persistence.DefaultPasswordConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.impl.specification.DefaultPasswordConfigurationSpecifications;
import com.emmisolutions.emmimanager.persistence.repo.DefaultPasswordConfigurationRepository;

/**
 * Persistence Implementation to deal with DefaultPasswordConfiguration
 */
@Repository
public class DefaultPasswordConfigurationPersistenceImpl implements
        DefaultPasswordConfigurationPersistence {

    @Resource
    DefaultPasswordConfigurationRepository defaultPasswordConfigurationRepository;

    @Resource
    DefaultPasswordConfigurationSpecifications defaultPasswordConfigurationSpecifications;

    @Override
    public DefaultPasswordConfiguration findActive() {
        return defaultPasswordConfigurationRepository
                .findOne(where(defaultPasswordConfigurationSpecifications
                        .isActive()));
    }

    @Override
    public DefaultPasswordConfiguration reload(Long id) {
        return defaultPasswordConfigurationRepository.findOne(id);
    }

    @Override
    public DefaultPasswordConfiguration saveOrUpdate(
            DefaultPasswordConfiguration defaultPasswordConfiguration) {
        return defaultPasswordConfigurationRepository
                .save(defaultPasswordConfiguration);
    }

}
