package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ReferenceGroupService;
import org.junit.Test;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

/**
 * Integration tests for ReferenceGroupService
 */
public class ReferenceGroupServiceIntegrationTest extends BaseIntegrationTest {

    @Resource
    ReferenceGroupService referenceGroupService;

    /**
     * Make sure we can load a page of reference groups
     */
    @Test
    public void load() {
        assertThat("Reference Groups are loaded",
            referenceGroupService.loadReferenceGroups(null).getTotalElements(), is(not(0l)));
    }
}
