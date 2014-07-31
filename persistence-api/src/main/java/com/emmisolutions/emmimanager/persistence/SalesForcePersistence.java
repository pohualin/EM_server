package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.SalesForce;

/**
 * SalesForce persistence
 */
public interface SalesForcePersistence {

    /**
     * Save or update the object
     * @param salesForce to be save/updated
     * @return the save/updated object
     */
    SalesForce save(SalesForce salesForce);

    /**
     * Fetches from db
     * @param id to be fetched (by id)
     * @return SalesForce or null
     */
    SalesForce reload(Long id);
}
