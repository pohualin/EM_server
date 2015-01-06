package com.emmisolutions.emmimanager.model;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * The search filter for UserClient entities
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UserClientUserClientTeamRoleSearchFilter {

    private String term;

    private StatusFilter status;

    private Tag tag;

    private UserClient userClient;

    /**
     * Default constructor
     */
    public UserClientUserClientTeamRoleSearchFilter() {
        this.status = StatusFilter.ALL;
    }

    /**
     * Constructor with passed in userClient and search term
     * 
     * @param userClient
     *            to use
     * @param term
     *            to search
     */
    public UserClientUserClientTeamRoleSearchFilter(UserClient userClient,
            String term) {
        this(userClient, StatusFilter.ALL, term, null);
    }

    /**
     * Constructor with passed in userClient, status and search term
     * 
     * @param userClient
     *            to use
     * @param status
     *            to filter
     * @param term
     *            to search
     * @param tag
     *            to use
     */
    public UserClientUserClientTeamRoleSearchFilter(UserClient userClient,
            StatusFilter status, String term, Tag tag) {
        this.userClient = userClient;
        this.term = term;
        this.status = status;
        this.tag = tag;
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
     * Return tag
     * 
     * @return tag
     */
    public Tag getTag() {
        return tag;
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
                    if (statusFilter.toString().equalsIgnoreCase(status)) {
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
