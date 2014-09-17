package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.service.configuration.ServiceConfiguration;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * Root integration test harness
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback = true)
@ActiveProfiles("test")
//@Transactional - do not enable this.. the service implementation should be annotated correctly!
public abstract class BaseIntegrationTest {
	
}
