package com.emmisolutions.emmimanager.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * The search filter for UserClient entities
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UserClientSupportSearchFilter extends UserClientCommonSearchFilter {

    /**
     * constructor
     */
    public UserClientSupportSearchFilter() {
        super();
    }

    /**
     * all status plus passed term
     */
    public UserClientSupportSearchFilter(String term) {
        super(term);
    }

    /**
     * constructor
     *
     * @param status
     *            to filter
     * @param term
     *            to filter
     */
    public UserClientSupportSearchFilter(StatusFilter status, String term) {
        super(status, term);
    }

    @Override
    public String toString() {
        return "UserClientSupportSearchFilter{" + "term=" + getTerm()
                + ", status=" + getStatus() + '}';
    }
}
