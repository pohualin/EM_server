package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ReferenceGroupPersistence;
import org.junit.Test;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

/**
 * Reference Group integration tests
 */
public class ReferenceGroupPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    ReferenceGroupPersistence referenceGroupPersistence;

    @Test
    public void load(){
        assertThat("es is not present",
            referenceGroupPersistence.loadReferenceGroups(null).getTotalElements(),
            is(not(0l)));
    }
}
