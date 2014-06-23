package com.emmisolutions.emmimanager.service;

import liquibase.configuration.GlobalConfiguration;
import org.junit.BeforeClass;


public abstract class BaseUnitTest {

    @BeforeClass
    public static void switchOffLiquibase() {
        System.setProperty(GlobalConfiguration.SHOULD_RUN, "false");
    }
}
