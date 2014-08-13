package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.persistence.SalesForcePersistence;
import com.emmisolutions.emmimanager.persistence.repo.SalesForceRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Implementation of SalesForcePersistence
 */
@Repository
public class SalesForcePersistenceImpl implements SalesForcePersistence{

    @Resource
    SalesForceRepository salesForceRepository;

    @Override
    public List<SalesForce> findByAccountNumbers(Set<String> accountNumbersToMatch) {
        if (CollectionUtils.isEmpty(accountNumbersToMatch)) {
            return new ArrayList<>();
        }
        return salesForceRepository.findByAccountNumberIn(accountNumbersToMatch);
    }
}
