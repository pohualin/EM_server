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

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final String ID_QUERY = "SELECT Account.Id, Account.Name, Account.EMMI_Account_ID__c, Account.Type, Account.RecordTypeId, Account.RecordType.Name, Account.Account_Status__c, Account.BillingCity, Account.BillingState, Account.BillingPostalCode, Account.BillingStreet, Account.BillingCountry, Account.Owner.Id, Account.Owner.Alias, Account.Phone, Account.Fax FROM Account WHERE Id = '%s'";
    private static final Pattern ID_PATTERN = Pattern.compile("[a-zA-Z0-9]{18}");
    private static final String FIND_QUERY = "FIND {%s} RETURNING Account(Id, Name, EMMI_Account_ID__c, RecordTypeId, RecordType.Name, Account_Status__c, BillingCity, BillingState, BillingPostalCode, BillingStreet, BillingCountry, Owner.Id, Owner.Alias,Phone, Fax ORDER BY Name LIMIT %s)";
    private static final String ESCAPE_CHARS = "?&|!{}[]()^~*:\\'+-";

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

    public SalesForceSearchResponse findAccounts(String searchString) {
        return findAccounts(searchString, DEFAULT_PAGE_SIZE);
    }

    public SalesForceSearchResponse findAccounts(String searchString, int pageSizeRequested) {
        String strippedSearchString = StringUtils.stripToNull(searchString);

        // make sure a filter is there
        if (StringUtils.length(strippedSearchString) < 5) {
            return new SalesForceSearchResponse(true, new ArrayList<SalesForce>());
        }

        // set the default page size if one is not provided
        int pageSize;
        if (pageSizeRequested <= 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        } else {
            pageSize = pageSizeRequested;
        }

        List<SalesForce> accounts = new ArrayList<>();
        boolean isComplete;
        int totalNumber;
        try {
            // search the account
            SearchResult searchResult = connection.search(String.format(FIND_QUERY, escape(strippedSearchString), pageSize + 1));
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
            } else if (ID_PATTERN.matcher(strippedSearchString).matches()) {
                // no search match, try to query by Id (when search string is a valid id)
                QueryResult queryResult = connection.query(String.format(ID_QUERY, strippedSearchString));
                if (queryResult.getSize() > 0) {
                    accounts.add(convert((Account) queryResult.getRecords()[0]));
                    totalNumber = 1;
                }
            }
            isComplete = totalNumber <= pageSize;
        } catch (ConnectionException e) {
            throw new RuntimeException(e);
        }
        return new SalesForceSearchResponse(isComplete, accounts);
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
