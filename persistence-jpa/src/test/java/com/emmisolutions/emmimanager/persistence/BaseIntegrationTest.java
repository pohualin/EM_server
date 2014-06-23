package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.persistence.configuration.PersistenceConfiguration;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ContextConfiguration(classes = PersistenceConfiguration.class)
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback = true)
@ActiveProfiles("test")
@Transactional
public abstract class BaseIntegrationTest {

    @PersistenceContext(unitName = "EmmiManagerPersistenceUnit")
    protected EntityManager entityManager;

}
