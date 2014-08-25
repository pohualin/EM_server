package com.emmisolutions.emmimanager.persistence;

import liquibase.configuration.GlobalConfiguration;
import org.junit.BeforeClass;

/**
 * Base unit testing harness. You should be mocking all dependencies when this is extended.
 */
public abstract class BaseUnitTest {

    /**
     * Don't create/mod the database for unit tests
     */
    @BeforeClass
    public static void switchOffLiquibase() {
        System.setProperty(GlobalConfiguration.SHOULD_RUN, "false");
    }
}
