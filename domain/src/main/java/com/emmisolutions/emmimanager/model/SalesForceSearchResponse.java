package com.emmisolutions.emmimanager.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

/**
 * A search response for accounts
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SalesForceSearchResponse {

    private boolean complete;

    private int total;

    @XmlElement(name = "account")
    @XmlElementWrapper(name="accounts")
    private List<SalesForce> accounts;

    public SalesForceSearchResponse(boolean complete, int total, List<SalesForce> accounts) {
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

    public List<SalesForce> getAccounts() {
        return accounts;
    }
}
