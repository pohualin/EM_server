package com.emmisolutions.emmimanager.salesforce;

import com.emmisolutions.emmimanager.salesforce.configuration.SalesForceConfiguration;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Root integration test harness
 */
@ContextConfiguration(classes = SalesForceConfiguration.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {
}
