package com.emmisolutions.emmimanager.salesforce.service;

import com.emmisolutions.emmimanager.model.SalesForceSearchResponse;
import com.emmisolutions.emmimanager.salesforce.BaseIntegrationTest;
import org.junit.Test;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Integration test
 */
public class LookupIntegrationTest extends BaseIntegrationTest {

    private static final String SEARCH_RESPONSE_NOT_NULL = "search response is not null";
    private static final String SHOULD_HAVE_MORE_RESULTS = "should be more results";
    private static final String SHOULD_HAVE_NO_MORE_RESULTS = "should be more results";

    @Resource
    SalesForceLookup salesForceLookup;

    /**
     * Find
     */
    @Test
    public void find() {
        SalesForceSearchResponse searchResponse = salesForceLookup.findAccounts("magee hospital upmc");
        assertThat(SEARCH_RESPONSE_NOT_NULL, searchResponse, is(notNullValue()));
    }

    /**
     * Find paged
     */
    @Test
    public void findPaged() {
        SalesForceSearchResponse searchResponse = salesForceLookup.findAccounts("magee hospital", 10);
        assertThat(SEARCH_RESPONSE_NOT_NULL, searchResponse, is(notNullValue()));
        assertThat(SHOULD_HAVE_MORE_RESULTS, searchResponse.isComplete(), is(false));
        assertThat("total should be 10", searchResponse.getAccounts().size(), is(10));
    }

    /**
     * find by id should not work
     */
    @Test
    public void findById() {
        SalesForceSearchResponse searchResponse = salesForceLookup.findAccounts("0013000000CqY7OAAV");
        assertThat(SEARCH_RESPONSE_NOT_NULL, searchResponse, is(notNullValue()));
        assertThat(SHOULD_HAVE_NO_MORE_RESULTS, searchResponse.isComplete(), is(true));
        assertThat("total should be 0", searchResponse.getAccounts().size(), is(0));
    }

    /**
     * find with complexish query
     */
    @Test
    public void findByComplexQuery() {
        SalesForceSearchResponse searchResponse = salesForceLookup.findAccounts("\"Minimally Invasive Bariatric\" AND Pittsburgh");
        assertThat(SEARCH_RESPONSE_NOT_NULL, searchResponse, is(notNullValue()));
        assertThat(SHOULD_HAVE_MORE_RESULTS, searchResponse.isComplete(), is(true));
        assertThat("total should be one", searchResponse.getAccounts().size(), is(1));
    }
}
