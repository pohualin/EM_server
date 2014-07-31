package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.persistence.SalesForcePersistence;
import com.emmisolutions.emmimanager.service.SalesForceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * SalesForce Service Implementation
 */
@Service
public class SalesForceServiceImpl implements SalesForceService {

    @Resource
    SalesForcePersistence salesForcePersistence;

    @Override
    @Transactional
    public SalesForce create(SalesForce salesForce) {
        salesForce.setId(null);
        salesForce.setVersion(null);
        return salesForcePersistence.save(salesForce);
    }

    @Override
    @Transactional
    public SalesForce update(SalesForce salesForce) {
        if (salesForce == null || salesForce.getId() == null || salesForce.getVersion() == null){
            throw new IllegalArgumentException("SalesForce Id and Version cannot be null.");
        }
        return salesForcePersistence.save(salesForce);
    }

    @Override
    @Transactional(readOnly = true)
    public SalesForce reload(SalesForce salesForce) {
        if (salesForce == null || salesForce.getId() == null) {
            return null;
        }
        return salesForcePersistence.reload(salesForce.getId());
    }
}
