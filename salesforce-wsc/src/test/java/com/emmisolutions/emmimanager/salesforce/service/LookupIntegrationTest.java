package com.emmisolutions.emmimanager.salesforce.service;

import com.emmisolutions.emmimanager.salesforce.BaseIntegrationTest;
import com.emmisolutions.emmimanager.salesforce.model.AccountSearchResponse;
import org.junit.Test;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class LookupIntegrationTest extends BaseIntegrationTest {

    @Resource
    SalesForce salesForce;

    @Test
    public void find(){
        AccountSearchResponse searchResponse = salesForce.findAccountsByName("a");
        assertThat("search response is not null", searchResponse, is(notNullValue()));
    }

    @Test
    public void findPaged(){
        AccountSearchResponse searchResponse = salesForce.findAccountsByName("a", 250);
        assertThat("search response is not null", searchResponse, is(notNullValue()));
        assertThat("should be more results", searchResponse.isComplete(), is(false));
        assertThat("total should be more than 2", searchResponse.getTotal(), is(not(250)));
    }
}
