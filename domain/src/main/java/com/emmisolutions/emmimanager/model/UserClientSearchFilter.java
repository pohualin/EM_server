package com.emmisolutions.emmimanager.model;

import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * The search filter for UserClient entities
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UserClientSearchFilter {

    private String term;

    private StatusFilter status;

    private Client client;

    private Team team;

    private Tag tag;

    /**
     * constructor
     */
    public UserClientSearchFilter() {
        this.status = StatusFilter.ALL;
    }

    /**
     * all status plus passed term
     */
    public UserClientSearchFilter(Client client, String term) {
        this(client, StatusFilter.ALL, term);
    }

    /**
     * constructor
     *
     * @param status to filter
     * @param term   to filter
     */
    public UserClientSearchFilter(Client client, StatusFilter status,
                                  String term) {
        this.client = client;
        this.term = term;
        this.status = status;
    }

    public String getTerm() {
        return term;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public StatusFilter getStatus() {
        return status;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

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
         * @param status the status string
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
