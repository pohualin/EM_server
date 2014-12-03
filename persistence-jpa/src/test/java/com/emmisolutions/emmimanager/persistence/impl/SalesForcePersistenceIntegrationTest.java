package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.SalesForcePersistence;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.HashSet;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * SalesForce persistence tests
 */
public class SalesForcePersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    SalesForcePersistence salesForcePersistence;

    /**
     * A null query should return empty set
     */
    @Test
    public void nullQuery() {
        assertThat("make sure an empty set comes back, not null",
            salesForcePersistence.findByAccountNumbers(null).isEmpty(), is(true));
    }

    /**
     * Ensure that the table is there and can be read
     */
    @Test
    public void makeTheQuery() {
        assertThat("make sure an empty set comes back, not null",
            salesForcePersistence.findByAccountNumbers(new HashSet<String>(){{
                add("whatever");
            }}).isEmpty(), is(true));
    }
}
