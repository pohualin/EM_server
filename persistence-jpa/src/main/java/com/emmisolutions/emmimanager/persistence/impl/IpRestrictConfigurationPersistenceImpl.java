package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.configuration.IpRestrictConfiguration;
import com.emmisolutions.emmimanager.persistence.IpRestrictConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.impl.specification.IpRestrictConfigurationSpecifications;
import com.emmisolutions.emmimanager.persistence.repo.IpRestrictConfigurationRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

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
    @CacheEvict(value = "ipRestrictConfigurationByClient", allEntries = true)
    public void delete(Long id) {
        ipRestrictConfigurationRepository.delete(id);
    }

    @Override
    @Cacheable(value = "ipRestrictConfigurationByClient")
    public Page<IpRestrictConfiguration> list(Pageable pageable,
            Long clientId) {
        Pageable toUse = pageable;
        if (pageable == null) {
            toUse = new PageRequest(0, 10, new Sort(Sort.Direction.ASC, "ipRangeStart"));
        }
        return ipRestrictConfigurationRepository
                .findAll(
                        where(ipRestrictConfigurationSpecifications
                                .isClient(clientId)),
                        toUse);
    }

    @Override
    public IpRestrictConfiguration reload(Long id) {
        return ipRestrictConfigurationRepository.findOne(id);
    }

    @Override
    @CacheEvict(value = "ipRestrictConfigurationByClient", allEntries = true)
    public IpRestrictConfiguration saveOrUpdate(
            IpRestrictConfiguration ipRestrictConfiguration) {
        return ipRestrictConfigurationRepository.save(ipRestrictConfiguration);
    }

}
