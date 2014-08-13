package com.emmisolutions.emmimanager.salesforce.wsc;

import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.model.SalesForceSearchResponse;
import com.emmisolutions.emmimanager.salesforce.service.SalesForceLookup;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.SearchRecord;
import com.sforce.soap.enterprise.SearchResult;
import com.sforce.soap.enterprise.sobject.Account;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Lookup implementation that uses WSC to query SalesForce.
 */
@Repository
public class Lookup implements SalesForceLookup {

    private static final int DEFAULT_PAGE_SIZE = 50;
    private static final String ID_QUERY = "SELECT Account.Id, Account.Name, Account.EMMI_Account_ID__c, Account.Type, Account.RecordTypeId, Account.RecordType.Name, Account.Account_Status__c, Account.BillingCity, Account.BillingState, Account.BillingPostalCode, Account.BillingStreet, Account.BillingCountry, Account.Owner.Id, Account.Owner.Alias, Account.Phone, Account.Fax FROM Account WHERE Id = '%s'";
    private static final Pattern ID_PATTERN = Pattern.compile("[a-zA-Z0-9]{18}");
    private static final String FIND_QUERY = "FIND {%s} RETURNING Account(Id, Name, EMMI_Account_ID__c, RecordTypeId, RecordType.Name, Account_Status__c, BillingCity, BillingState, BillingPostalCode, BillingStreet, BillingCountry, Owner.Id, Owner.Alias,Phone, Fax ORDER BY Name)";

    private EnterpriseConnection connection;

    @Value("${salesforce.username}")
    private String username;

    @Value("${salesforce.password}")
    private String password;

    @Value("${salesforce.url}")
    private String url;

    @PostConstruct
    private void init() throws ConnectionException {
        ConnectorConfig config = new ConnectorConfig();
        config.setUsername(username);
        config.setPassword(password);
        config.setAuthEndpoint(url);
        connection = new EnterpriseConnection(config);
    }

    public SalesForceSearchResponse findAccountsByNameOrId(String searchString) {
        return findAccountsByNameOrId(searchString, DEFAULT_PAGE_SIZE);
    }

    public SalesForceSearchResponse findAccountsByNameOrId(String searchString, int pageSize) {
        searchString = StringUtils.stripToNull(searchString);

        // make sure a filter is there
        if (StringUtils.length(searchString) < 5) {
            return new SalesForceSearchResponse(true, 0, new ArrayList<SalesForce>());
        }

        // set the default page size if one is not provided
        if (pageSize <= 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        List<SalesForce> accounts = new ArrayList<>();
        boolean isComplete;
        int totalNumber;
        try {
            // search the account
            SearchResult searchResult = connection.search(String.format(FIND_QUERY, searchString));
            totalNumber = searchResult.getSearchRecords().length;
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
            } else if (ID_PATTERN.matcher(searchString).matches()) {
                // no search match, try to query by Id (when search string is a valid id)
                QueryResult queryResult = connection.query(String.format(ID_QUERY, searchString));
                if (queryResult.getSize() > 0) {
                    accounts.add(convert((Account) queryResult.getRecords()[0]));
                    totalNumber = 1;
                }
            }
            isComplete = totalNumber <= pageSize;
        } catch (ConnectionException e) {
            throw new RuntimeException(e);
        }
        return new SalesForceSearchResponse(isComplete, totalNumber, accounts);
    }
    
    private SalesForce convert(Account account) {
        SalesForce sf = new SalesForce();
        sf.setAccountNumber(account.getId());
        sf.setName(account.getName());
        sf.setStreet(account.getBillingStreet());
        sf.setState(account.getBillingState());
        sf.setCountry(account.getBillingCountry());
        sf.setPostalCode(account.getBillingPostalCode());
        sf.setPhoneNumber(account.getPhone());
        return sf;
    }

}
