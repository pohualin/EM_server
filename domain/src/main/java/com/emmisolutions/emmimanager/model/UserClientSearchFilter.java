package com.emmisolutions.emmimanager.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.apache.commons.lang3.StringUtils;

/**
 * The search filter for UserClient entities
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UserClientSearchFilter {

    private String term;

    private StatusFilter status;

    private Long clientId;

    /**
     * constructor
     */
    public UserClientSearchFilter() {
	this.status = StatusFilter.ALL;
    }

    /**
     * all status plus passed term
     * 
     */
    public UserClientSearchFilter(Long clientId, String term) {
	this(clientId, StatusFilter.ALL, term);
    }

    /**
     * constructor
     * 
     * @param status
     *            to filter
     * @param term
     *            to filter
     */
    public UserClientSearchFilter(Long clientId, StatusFilter status,
	    String term) {
	this.clientId = clientId;
	this.term = term;
	this.status = status;
    }

    public String getTerm() {
	return term;
    }

    public Long getClientId() {
	return clientId;
    }

    public void setClientId(Long clientId) {
	this.clientId = clientId;
    }

    public StatusFilter getStatus() {
	return status;
    }

    /**
     * Status allowed
     */
    public enum StatusFilter {
	ALL, ACTIVE_ONLY, INACTIVE_ONLY;

	/**
	 * from string or ACTIVE
	 * 
	 * @param status
	 *            the status string
	 * @return never null, the status or ACTIVE
	 */
	public static StatusFilter fromStringOrActive(String status) {
	    if (StringUtils.isNotBlank(status)) {
		for (StatusFilter statusFilter : values()) {
		    if (statusFilter.toString().equals(status.toUpperCase())) {
			return statusFilter;
		    }
		}
	    }
	    return ACTIVE_ONLY;
	}
    }

    @Override
    public String toString() {
	return "UserClientSearchFilter{" + "term=" + term + ", status="
		+ status + '}';
    }
}
