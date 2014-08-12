package com.emmisolutions.emmimanager.salesforce.wsc;

import com.emmisolutions.emmimanager.salesforce.model.AccountSearchResponse;
import com.emmisolutions.emmimanager.salesforce.model.SalesForceAccount;
import com.emmisolutions.emmimanager.salesforce.service.SalesForce;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Account;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Lookup implementation that uses WSC to query SalesForce.
 */
@Repository
public class Lookup implements SalesForce {

    EnterpriseConnection connection;

    private static final int DEFAULT_PAGE_SIZE = 200;

    private final String queryFragment =  "SELECT Account.Id, " +
            "Account.Name, " +
            "Account.EMMI_Account_ID__c, " +
            "Account.Type, " +
            "Account.RecordTypeId, " +
            "Account.RecordType.Name, " +
            "Account.Account_Status__c, " +
            "Account.BillingCity, " +
            "Account.BillingState, " +
            "Account.BillingPostalCode, " +
            "Account.BillingStreet, " +
            "Account.BillingCountry, " +
            "Account.Owner.Id, " +
            "Account.Owner.Alias, " +
            "Account.Phone, " +
            "Account.Fax " +
            "FROM Account ";

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

    public AccountSearchResponse findAccountsByName(String accountName){
        return findAccountsByName(accountName, DEFAULT_PAGE_SIZE);
    }

    public AccountSearchResponse findAccountsByName(String accountName, int pageSize) {

        // make sure a filter is there
        if (StringUtils.isBlank(accountName)){
            return new AccountSearchResponse(true, 0, new ArrayList<SalesForceAccount>());
        }

        // set the default page size if one is not provided
        if (pageSize <= DEFAULT_PAGE_SIZE){
            pageSize = DEFAULT_PAGE_SIZE;
        }

        // do the search
        List<SalesForceAccount> accounts = new ArrayList<>();
        String where = "WHERE Name like '%" + accountName + "%'";
        boolean isComplete = true;
        int totalNumber = 0;
        try {
            connection.setQueryOptions(pageSize);
            QueryResult qr = connection.query(queryFragment + where);
            for (SObject acct : qr.getRecords()) {
                accounts.add(new SalesForceAccount((Account)acct));
            }
            isComplete = qr.isDone();
            totalNumber = qr.getSize();
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
        return new AccountSearchResponse(isComplete, totalNumber, accounts);
    }

}
