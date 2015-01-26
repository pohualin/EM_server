package com.emmisolutions.emmimanager.persistence.impl;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.configuration.IpRestrictConfiguration;
import com.emmisolutions.emmimanager.persistence.IpRestrictConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.impl.specification.IpRestrictConfigurationSpecifications;
import com.emmisolutions.emmimanager.persistence.repo.IpRestrictConfigurationRepository;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * Persistence implementation for IpRestrictConfiguration entity.
 */
@Repository
public class IpRestrictConfigurationPersistenceImpl implements
        IpRestrictConfigurationPersistence {

    @Resource
    IpRestrictConfigurationRepository ipRestrictConfigurationRepository;

    @Resource
    IpRestrictConfigurationSpecifications ipRestrictConfigurationSpecifications;

    @Override
    public void delete(Long id) {
        ipRestrictConfigurationRepository.delete(id);
    }

    @Override
    public Page<IpRestrictConfiguration> list(Pageable pageable,
            Long clientRestrictConfigurationId) {
        Pageable toUse = pageable;
        if (pageable == null) {
            toUse = new PageRequest(0, 10);
        }
        return ipRestrictConfigurationRepository
                .findAll(
                        where(ipRestrictConfigurationSpecifications
                                .isClientRestrictConfiguration(clientRestrictConfigurationId)),
                        toUse);
    }

    @Override
    public IpRestrictConfiguration reload(Long id) {
        return ipRestrictConfigurationRepository.findOne(id);
    }

    @Override
    public IpRestrictConfiguration saveOrUpdate(
            IpRestrictConfiguration ipRestrictConfiguration) {
        return ipRestrictConfigurationRepository.save(ipRestrictConfiguration);
    }

}
