package com.emmisolutions.emmimanager.model;

import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * The search filter for UserClient entities
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class UserClientCommonSearchFilter {

    private String term;

    private StatusFilter status;

    /**
     * constructor
     */
    public UserClientCommonSearchFilter() {
        this.status = StatusFilter.ALL;
    }

    /**
     * all status plus passed term
     */
    public UserClientCommonSearchFilter(String term) {
        this(StatusFilter.ALL, term);
    }

    /**
     * constructor
     *
     * @param status
     *            to filter
     * @param term
     *            to filter
     */
    public UserClientCommonSearchFilter(StatusFilter status, String term) {
        this.term = term;
        this.status = status;
    }

    public String getTerm() {
        return term;
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
        return "UserClientCommonSearchFilter{" + "term=" + term + ", status="
                + status + '}';
    }
}
