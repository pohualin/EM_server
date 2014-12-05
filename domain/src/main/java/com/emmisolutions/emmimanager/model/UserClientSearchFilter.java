package com.emmisolutions.emmimanager.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.apache.commons.lang3.StringUtils;

/**
 * The search filter for UserClient entities
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UserClientSearchFilter {

    @XmlElement(name = "name")
    @XmlElementWrapper(name = "names")
    private Set<String> names;

    private StatusFilter status;

    private Long clientId;

    /**
     * constructor
     */
    public UserClientSearchFilter() {
	this.status = StatusFilter.ALL;
    }

    /**
     * all status plus passed names
     * 
     * @param names
     *            filter
     */
    public UserClientSearchFilter(Long clientId, String... names) {
	this(clientId, StatusFilter.ALL, names);
    }

    /**
     * constructor
     * 
     * @param status
     *            to filter
     * @param names
     *            to filter
     */
    public UserClientSearchFilter(Long clientId, StatusFilter status,
	    String... names) {
	if (clientId != null) {
	    this.clientId = clientId;
	}
	if (names != null) {
	    this.names = new HashSet<>();
	    Collections.addAll(this.getNames(), names);
	}
	if (status != null) {
	    this.status = status;
	}
    }

    public Set<String> getNames() {
	return names;
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
	return "UserClientSearchFilter{" + "names=" + names + ", status="
		+ status + '}';
    }
}
