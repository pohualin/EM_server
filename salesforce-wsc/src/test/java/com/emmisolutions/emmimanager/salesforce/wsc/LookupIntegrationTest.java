package com.emmisolutions.emmimanager.salesforce.wsc;

import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.model.SalesForceSearchResponse;
import com.emmisolutions.emmimanager.model.salesforce.IdNameLookupResultContainer;
import com.emmisolutions.emmimanager.salesforce.BaseIntegrationTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Integration test
 */
public class LookupIntegrationTest extends BaseIntegrationTest {

    private static final String SEARCH_RESPONSE_NOT_NULL = "search response is not null";
    private static final String SHOULD_HAVE_MORE_RESULTS = "should be more results";
    private static final String SHOULD_HAVE_NO_MORE_RESULTS = "should be more results";
    private static final String RESULTS_SHOULD_BE_FOUND = "search response should have found at least one element";

    private static final Logger LOGGER = LoggerFactory.getLogger(LookupIntegrationTest.class);

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
     * find by id should work
     */
    @Test
    public void findByIdWorks() {
        SalesForce searchResponse = salesForceLookup.findAccountById("0015000000YUCQEAA5");
        assertThat(SEARCH_RESPONSE_NOT_NULL, searchResponse, is(notNullValue()));
        assertThat(SEARCH_RESPONSE_NOT_NULL, searchResponse.getName(), is(notNullValue()));
    }

    /**
     * find by id should not work - invalid id
     */
    @Test
    public void findByIdInvalidWorks() {
        SalesForce searchResponse = salesForceLookup.findAccountById("001dfghjkghj");
        assertThat(SEARCH_RESPONSE_NOT_NULL, searchResponse, is(nullValue()));
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

    @Test
    public void attemptToFindBlank() {
        SalesForceSearchResponse searchResponse = salesForceLookup.findAccounts("        ");
        assertThat(SEARCH_RESPONSE_NOT_NULL, searchResponse, is(notNullValue()));
        assertThat(SHOULD_HAVE_MORE_RESULTS, searchResponse.isComplete(), is(true));
        assertThat("total should be zero", searchResponse.getAccounts().size(), is(0));
    }

    @Test
    public void genericFind() {
        assertThat(RESULTS_SHOULD_BE_FOUND,
                salesForceLookup.find("matt", 5, "Contact").hasResults(), is(true));
        assertThat(RESULTS_SHOULD_BE_FOUND,
                salesForceLookup.find("magee", 5, "Account").hasResults(), is(true));
        assertThat(RESULTS_SHOULD_BE_FOUND,
                salesForceLookup.find("jeff", 5, "Group", "User").hasResults(), is(true));
        assertThat(RESULTS_SHOULD_BE_FOUND,
                salesForceLookup.find("Anti", 5, "Program__c").hasResults(), is(true));
    }
}
