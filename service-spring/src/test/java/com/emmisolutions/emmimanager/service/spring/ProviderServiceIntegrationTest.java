package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ProviderService;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Provider Service Base Functions
 */
public class ProviderServiceIntegrationTest extends BaseIntegrationTest {

    @Resource
    ProviderService providerService;

    /**
     * Hit the list method to make sure it works
     */
    @Test
    public void list(){
        Provider provider = makeNewRandomProvider();
        assertThat("list method works",
            providerService.list(null, null), hasItem(provider));
    }

    /**
     * Update of bad provider should fail
     */
    @Test(expected = IllegalArgumentException.class)
    public void badUpdate(){
        providerService.update(new Provider());
    }

    /**
     * Find specialties
     */
    @Test
    public void findSpecialties(){
        assertThat("Specialties are loaded",
            providerService.findAllSpecialties(null).getTotalElements(), is(not(0l)));
    }

    /**
     * Bad create
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void createNull(){
        providerService.create(null);
    }
}
