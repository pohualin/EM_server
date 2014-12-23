package com.emmisolutions.emmimanager.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.apache.commons.lang3.StringUtils;

import com.emmisolutions.emmimanager.model.user.client.UserClient;

/**
 * The search filter for UserClient entities
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UserClientUserClientTeamRoleSearchFilter {

    private String term;

    private StatusFilter status;

    private UserClient userClient;

    /**
     * Default constructor
     */
    public UserClientUserClientTeamRoleSearchFilter() {
	this.status = StatusFilter.ALL;
    }

    /**
     * all status plus passed term
     * 
     */
    public UserClientUserClientTeamRoleSearchFilter(UserClient userClient,
	    String term) {
	this(userClient, StatusFilter.ALL, term);
    }

    /**
     * Constructor takes client, status and term
     * 
     * @param client
     * @param status
     * @param term
     */
    public UserClientUserClientTeamRoleSearchFilter(UserClient userClient,
	    StatusFilter status, String term) {
	this.userClient = userClient;
	this.term = term;
	this.status = status;
    }

    /**
     * Return term
     * 
     * @return term
     */
    public String getTerm() {
	return term;
    }

    /**
     * Return status
     * 
     * @return status
     */
    public StatusFilter getStatus() {
	return status;
    }

    /**
     * Return userClient
     * 
     * @return userClient
     */
    public UserClient getUserClient() {
	return userClient;
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
