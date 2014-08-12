package com.emmisolutions.emmimanager.salesforce.model;

import java.util.List;

/**
 * A search response for accounts
 */
public class AccountSearchResponse {

    private boolean complete;

    private int total;

    private List<SalesForceAccount> accounts;

    public AccountSearchResponse(boolean complete, int total, List<SalesForceAccount> accounts) {
        this.complete = complete;
        this.total = total;
        this.accounts = accounts;
    }

    public boolean isComplete() {
        return complete;
    }

    public int getTotal() {
        return total;
    }

    public List<SalesForceAccount> getAccounts() {
        return accounts;
    }
}
