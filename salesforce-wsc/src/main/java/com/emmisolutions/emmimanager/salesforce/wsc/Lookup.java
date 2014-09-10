package com.emmisolutions.emmimanager.salesforce.wsc;

import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.model.SalesForceSearchResponse;
import com.emmisolutions.emmimanager.salesforce.service.SalesForceLookup;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.SearchRecord;
import com.sforce.soap.enterprise.SearchResult;
import com.sforce.soap.enterprise.sobject.Account;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Lookup implementation that uses WSC to query SalesForce.
 */
@Repository
public class Lookup implements SalesForceLookup {

    private static final Logger LOGGER = LoggerFactory.getLogger(Lookup.class);

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final String FIND_QUERY = "FIND {%s} RETURNING Account(Id, Name, EMMI_Account_ID__c, RecordTypeId, RecordType.Name, Account_Status__c, BillingCity, BillingState, BillingPostalCode, BillingStreet, BillingCountry, Owner.Id, Owner.Alias,Phone, Fax ORDER BY Name LIMIT %s)";
    private static final String ESCAPE_CHARS = "?&|!{}[]()^~*:\\'+-";

    private EnterpriseConnection connection;

    @Value("${salesforce.username}")
    private String username;

    @Value("${salesforce.password}")
    private String password;

    @Value("${salesforce.url}")
    private String url;

    private synchronized void init() {
        try {
            if (connection == null) {
                LOGGER.debug("Attempting to connect to SalesForce");
                ConnectorConfig config = new ConnectorConfig();
                config.setUsername(username);
                config.setPassword(password);
                config.setAuthEndpoint(url);
                connection = new EnterpriseConnection(config);
            }
        } catch (ConnectionException e) {
            LOGGER.error("Error connecting to SalesForce", e);
        }
    }

    /**
     * Queries SalesForce for accounts using a default page size and a filter
     *
     * @param searchString the filter
     * @return a search response
     */
    public SalesForceSearchResponse findAccounts(String searchString) {
        return findAccounts(searchString, DEFAULT_PAGE_SIZE);
    }

    /**
     * Queries SalesForce for accounts
     *
     * @param searchString the filter
     * @param pageSizeRequested     the number of objects to request from SalesForce
     * @return a search response
     */
    public SalesForceSearchResponse findAccounts(String searchString, int pageSizeRequested) {
        if (connection == null) {
            init();
            if (connection == null) {
                LOGGER.error("No Connection to SalesForce present, unable to process search request for '{}'", searchString);
                return new SalesForceSearchResponse(true, new ArrayList<SalesForce>());
            }
        }

        String strippedSearchString = StringUtils.stripToNull(searchString);

        // make sure a filter is there
        if (StringUtils.length(strippedSearchString) < 5) {
            return new SalesForceSearchResponse(true, new ArrayList<SalesForce>());
        }

        int pageSize;
        if (pageSizeRequested <= 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        } else {
            pageSize = pageSizeRequested;
        }

        List<SalesForce> accounts = new ArrayList<>();
        int totalNumber;

        // perform a search for the term
        SearchResult searchResult = null;
        try {
            searchResult = connection.search(String.format(FIND_QUERY, escape(strippedSearchString), pageSize + 1));
        } catch (ConnectionException e) {
            connection = null;
            findAccounts(searchString, pageSizeRequested);
        }

        totalNumber = searchResult != null ? searchResult.getSearchRecords().length : 0;
        if (totalNumber > 0) {
            // found a match
            int count = 0;
            for (SearchRecord o : searchResult.getSearchRecords()) {
                accounts.add(convert((Account) o.getRecord()));
                count++;
                if (count >= pageSize) {
                    break;
                }
            }
        }
        return new SalesForceSearchResponse(totalNumber <= pageSize, accounts);
    }

    private String escape(String searchQuery) {
        StringBuilder ret = new StringBuilder();
        for (Character character : searchQuery.toCharArray()) {
            if (ESCAPE_CHARS.contains(character.toString())) {
                ret.append('\\').append(character);
            } else {
                ret.append(character);
            }
        }
        return ret.toString();
    }

    private SalesForce convert(Account account) {
        SalesForce sf = new SalesForce();
        sf.setAccountNumber(account.getId());
        sf.setName(account.getName());
        sf.setStreet(account.getBillingStreet());
        sf.setState(account.getBillingState());
        sf.setCity(account.getBillingCity());
        sf.setCountry(account.getBillingCountry());
        sf.setPostalCode(account.getBillingPostalCode());
        sf.setPhoneNumber(account.getPhone());
        return sf;
    }

}
