package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.LocationPersistence;
import org.junit.Test;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

/**
 * Created by matt on 7/28/14.
 */
public class LocationPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    LocationPersistence locationPersistence;

    @Test(expected = ConstraintViolationException.class)
    public void saveLocation(){
       locationPersistence.save(new Location());
    }
}
