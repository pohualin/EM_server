package com.emmisolutions.emmimanager.salesforce.service;

import com.emmisolutions.emmimanager.model.SalesForceSearchResponse;
import com.emmisolutions.emmimanager.salesforce.BaseIntegrationTest;
import org.junit.Test;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class LookupIntegrationTest extends BaseIntegrationTest {

    @Resource
    SalesForceLookup salesForceLookup;

    @Test
    public void find(){
        SalesForceSearchResponse searchResponse = salesForceLookup.findAccountsByNameOrId("magee hospital upmc");
        assertThat("search response is not null", searchResponse, is(notNullValue()));
    }

    @Test
    public void findPaged(){
        SalesForceSearchResponse searchResponse = salesForceLookup.findAccountsByNameOrId("magee hospital", 10);
        assertThat("search response is not null", searchResponse, is(notNullValue()));
        assertThat("should be more results", searchResponse.isComplete(), is(false));
        assertThat("total should be 10", searchResponse.getAccounts().size(), is(10));
    }

    @Test
    public void findById(){
        SalesForceSearchResponse searchResponse = salesForceLookup.findAccountsByNameOrId("0013000000CqY7OAAV");
        assertThat("search response is not null", searchResponse, is(notNullValue()));
        assertThat("should be no more results", searchResponse.isComplete(), is(true));
        assertThat("total should be 1", searchResponse.getAccounts().size(), is(1));
    }

    @Test
    public void badId(){
        SalesForceSearchResponse searchResponse = salesForceLookup.findAccountsByNameOrId("013000000CqY7OAAV");
        assertThat("search response is not null", searchResponse, is(notNullValue()));
        assertThat("should be no more results", searchResponse.isComplete(), is(true));
        assertThat("total should be 0", searchResponse.getAccounts().size(), is(0));
    }

    @Test
    public void findByComplexQuery(){
        SalesForceSearchResponse searchResponse = salesForceLookup.findAccountsByNameOrId("\"Minimally Invasive Bariatric\" AND Pittsburgh");
        assertThat("search response is not null", searchResponse, is(notNullValue()));
        assertThat("should be more results", searchResponse.isComplete(), is(true));
        assertThat("total should be 1", searchResponse.getAccounts().size(), is(1));
    }
}
