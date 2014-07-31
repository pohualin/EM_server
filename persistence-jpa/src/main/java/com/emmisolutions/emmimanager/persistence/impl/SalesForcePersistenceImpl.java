package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.persistence.SalesForcePersistence;
import com.emmisolutions.emmimanager.persistence.repo.SalesForceRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Implementation of SalesForcePersistence
 */
@Repository
public class SalesForcePersistenceImpl implements SalesForcePersistence{

    @Resource
    SalesForceRepository salesForceRepository;

    @Override
    public SalesForce save(SalesForce salesForce) {
        return salesForceRepository.save(salesForce);
    }

    @Override
    public SalesForce reload(Long id) {
        return salesForceRepository.getOne(id);
    }
}
