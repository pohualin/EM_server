package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ProgramPersistence;
import org.junit.Test;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test to ensure programs are loaded properly
 */
public class ProgramPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    ProgramPersistence programPersistence;

    /**
     * Make sure the db is hooked up and sample data loads properly
     */
    @Test
    public void initialLoadGood() {
        assertThat("we can get a page", programPersistence.find(null).getTotalElements() > 0, is(true));
    }

}
