package com.emmisolutions.emmimanager.salesforce.service;

import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.salesforce.BaseIntegrationTest;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * Test stub for create case.. this is really just
 * useful for development and shouldn't be running in an automated way
 */
public class CreateCaseIntegrationTest extends BaseIntegrationTest {

    @Resource
    SalesForceCreateCase createCase;

    @Test
    public void create() {
        createCase.openCase(new SalesForce("whatever"));
    }

}
