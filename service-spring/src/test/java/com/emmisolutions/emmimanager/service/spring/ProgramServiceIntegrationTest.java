package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ProgramService;
import org.junit.Test;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test for the program service
 */
public class ProgramServiceIntegrationTest extends BaseIntegrationTest {

    @Resource
    ProgramService programService;

    @Test
    public void findAll() {
        assertThat("we can get a page", programService.find(null).getTotalElements() > 0, is(true));
    }

}
