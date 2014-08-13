package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.SalesForce;

import java.util.List;
import java.util.Set;

/**
 * SalesForce persistence
 */
public interface SalesForcePersistence {

    /**
     * Find persistent SalesForce accounts by account number
     * @param accountNumbersToMatch set of numbers to match
     * @return SalesForce objects
     */
    List<SalesForce> findByAccountNumbers(Set<String> accountNumbersToMatch);
}
