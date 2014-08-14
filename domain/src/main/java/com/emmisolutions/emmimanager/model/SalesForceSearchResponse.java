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

    @XmlElement(name = "account")
    @XmlElementWrapper(name = "accounts")
    private List<SalesForce> accounts;

    /**
     * Constructor
     *
     * @param complete is the response finished or are there more, true is finished
     * @param accounts in the current response
     */
    public SalesForceSearchResponse(boolean complete, List<SalesForce> accounts) {
        this.complete = complete;
        this.accounts = accounts;
    }

    public boolean isComplete() {
        return complete;
    }

    public List<SalesForce> getAccounts() {
        return accounts;
    }

    @Override
    public String toString() {
        return "SalesForceSearchResponse{" +
                "complete=" + complete +
                ", accounts=" + accounts +
                '}';
    }
}
