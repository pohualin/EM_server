package com.emmisolutions.emmimanager.salesforce.wsc;

import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.model.SalesForceSearchResponse;
import com.emmisolutions.emmimanager.model.salesforce.IdNameLookupResult;
import com.emmisolutions.emmimanager.model.salesforce.IdNameLookupResultContainer;
import com.emmisolutions.emmimanager.salesforce.service.SalesForceLookup;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.SearchRecord;
import com.sforce.soap.partner.SearchResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.emmisolutions.emmimanager.salesforce.wsc.ConnectionFactory.escape;

/**
 * Lookup implementation that uses WSC to query SalesForce.
 */
@Repository
public class Lookup implements SalesForceLookup {

    private static final Logger LOGGER = LoggerFactory.getLogger(Lookup.class);

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final String FIND_QUERY = "FIND {%s} RETURNING Account(Id, Name, EMMI_Account_ID__c, RecordTypeId, RecordType.Name, Account_Status__c, BillingCity, BillingState, BillingPostalCode, BillingStreet, BillingCountry, Owner.Id, Owner.Alias,Phone, Fax ORDER BY Name LIMIT %s)";
    private static final String ID_NAME_SOQL_QUERY = "SELECT Id, Name from %s WHERE Name like %s%s%s ORDER BY Name LIMIT %s";

    @Resource
    ConnectionFactory salesForceConnection;

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
     * @param searchString      the filter
     * @param pageSizeRequested the number of objects to request from SalesForce
     * @return a search response
     */
    public SalesForceSearchResponse findAccounts(String searchString, int pageSizeRequested) {
        return findAccounts(searchString, pageSizeRequested, true);
    }

    @Override
    public IdNameLookupResultContainer find(String searchString, int pageSizeRequested, String... types) {
        return find(searchString, pageSizeRequested, true, types);
    }

    private IdNameLookupResultContainer find(String searchString, int pageSizeRequested, boolean reUpConnection, String... types) {

        if (salesForceConnection.get() == null) {
            LOGGER.error("No Connection to SalesForce present, unable to process search request for '{}'", searchString);
            return new IdNameLookupResultContainer(true, new ArrayList<IdNameLookupResult>());
        }

        String strippedSearchString = StringUtils.stripToNull(searchString);

        // make sure a filter and types are present
        if (StringUtils.isBlank(strippedSearchString) || ArrayUtils.isEmpty(types)) {
            return new IdNameLookupResultContainer(true, new ArrayList<IdNameLookupResult>());
        }

        int pageSize;
        if (pageSizeRequested <= 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        } else {
            pageSize = pageSizeRequested;
        }

        int totalNumber = 0;
        List<IdNameLookupResult> lookupResults = new ArrayList<>();
        try {
            for (String type : types) {
                QueryResult queryResult = salesForceConnection.get()
                        .query(String.format(ID_NAME_SOQL_QUERY,
                                type, "'%", escape(strippedSearchString), "%'", pageSize + 1));

                if (queryResult != null) {
                    totalNumber += queryResult.getSize();
                    if (queryResult.getSize() > 0) {
                        // found a match
                        int count = 0;
                        for (SObject searchRecord : queryResult.getRecords()) {
                            count++;
                            lookupResults.add(new IdNameLookupResult(
                                    (String) searchRecord.getSObjectField("Id"),
                                    (String) searchRecord.getSObjectField("Name")));
                            if (count >= pageSize) {
                                break;
                            }
                        }
                    }
                }
                if (totalNumber >= pageSize) {
                    // no need to fetch the next object, already full
                    break;
                }
            }
        } catch (ConnectionException e) {
            if (reUpConnection) {
                salesForceConnection.reUp();
                find(searchString, pageSizeRequested, false, types);
            } else {
                LOGGER.error("SalesForce error", e);
            }
        }
        return new IdNameLookupResultContainer(totalNumber <= pageSize, lookupResults);
    }

    private SalesForceSearchResponse findAccounts(String searchString, int pageSizeRequested, boolean reUpConnection) {

        if (salesForceConnection.get() == null) {
            LOGGER.error("No Connection to SalesForce present, unable to process search request for '{}'", searchString);
            return new SalesForceSearchResponse(true, new ArrayList<SalesForce>());
        }

        String strippedSearchString = StringUtils.stripToNull(searchString);

        // make sure a filter is there
        if (StringUtils.isBlank(strippedSearchString)) {
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
            searchResult = salesForceConnection.get()
                    .search(String.format(FIND_QUERY, escape(strippedSearchString), pageSize + 1));
        } catch (ConnectionException e) {
            if (reUpConnection) {
                salesForceConnection.reUp();
                findAccounts(searchString, pageSizeRequested, false);
            } else {
                LOGGER.error("SalesForce error", e);
            }
        }

        totalNumber = searchResult != null ? searchResult.getSearchRecords().length : 0;
        if (totalNumber > 0) {
            // found a match
            int count = 0;
            for (SearchRecord o : searchResult.getSearchRecords()) {
                accounts.add(convert(o.getRecord()));
                count++;
                if (count >= pageSize) {
                    break;
                }
            }
        }
        return new SalesForceSearchResponse(totalNumber <= pageSize, accounts);
    }

    private SalesForce convert(SObject sObject) {
        SalesForce sf = new SalesForce();
        sf.setAccountNumber((String) sObject.getSObjectField("Id"));
        sf.setName((String) sObject.getSObjectField("Name"));
        sf.setStreet((String) sObject.getSObjectField("BillingStreet"));
        sf.setState((String) sObject.getSObjectField("BillingState"));
        sf.setCity((String) sObject.getSObjectField("BillingCity"));
        sf.setCountry((String) sObject.getSObjectField("BillingCountry"));
        sf.setPostalCode((String) sObject.getSObjectField("BillingPostalCode"));
        sf.setPhoneNumber((String) sObject.getSObjectField("Phone"));
        sf.setFax((String) sObject.getSObjectField("Fax"));
        return sf;
    }

}
