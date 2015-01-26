package com.emmisolutions.emmimanager.persistence.impl;

import static org.springframework.data.jpa.domain.Specifications.where;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.configuration.EmailRestrictConfiguration;
import com.emmisolutions.emmimanager.persistence.EmailRestrictConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.impl.specification.EmailRestrictConfigurationSpecifications;
import com.emmisolutions.emmimanager.persistence.repo.EmailRestrictConfigurationRepository;

/**
 * Persistence implementation for EmailRestrictConfiguration entity.
 */
@Repository
public class EmailRestrictConfigurationPersistenceImpl implements
        EmailRestrictConfigurationPersistence {

    @Resource
    EmailRestrictConfigurationRepository emailRestrictConfigurationRepository;

    @Resource
    EmailRestrictConfigurationSpecifications emailRestrictConfigurationSpecifications;

    @Override
    public void delete(Long id) {
        emailRestrictConfigurationRepository.delete(id);
    }

    @Override
    public Page<EmailRestrictConfiguration> list(Pageable pageable,
            Long clientRestrictConfigurationId) {
        Pageable toUse = pageable;
        if (pageable == null) {
            toUse = new PageRequest(0, 10);
        }
        return emailRestrictConfigurationRepository
                .findAll(
                        where(emailRestrictConfigurationSpecifications
                                .isClientRestrictConfiguration(clientRestrictConfigurationId)),
                        toUse);
    }

    @Override
    public EmailRestrictConfiguration reload(Long id) {
        return emailRestrictConfigurationRepository.findOne(id);
    }

    @Override
    public EmailRestrictConfiguration saveOrUpdate(
            EmailRestrictConfiguration emailRestrictConfiguration) {
        return emailRestrictConfigurationRepository
                .save(emailRestrictConfiguration);
    }

}
