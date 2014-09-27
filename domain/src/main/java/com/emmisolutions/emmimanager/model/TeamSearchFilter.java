package com.emmisolutions.emmimanager.model;

import org.apache.commons.lang3.StringUtils;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Filter used for Team searching.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class TeamSearchFilter {
	@XmlElement(name = "name")
    @XmlElementWrapper(name = "names")
    private Set<String> names;

    private StatusFilter status;

    private Long clientId;
    
    /**
     * Constructor
     */
    public TeamSearchFilter() {
        status = StatusFilter.ALL;
    }

    /**
     * Constructor
     * @param names to filter
     */
    public TeamSearchFilter(String... names) {
        this(StatusFilter.ALL, names);
    }

    /**
     * Constructor
     * @param status filter
     * @param names to filter
     */
    public TeamSearchFilter(StatusFilter status, String... names) {
        if (names != null) {
            this.names = new HashSet<>();
            Collections.addAll(this.names, names);
        }
        if (status != null) {
            this.status = status;
        }
    }
    
    public TeamSearchFilter(StatusFilter status, String names) {
        if (names != null) {
            this.names = new HashSet<>();
            Collections.addAll(this.names, names);
        }
        if (status != null) {
            this.status = status;
        }
    }

    public TeamSearchFilter(Long clientId, StatusFilter status, String... names) {
        this(status, names);
        this.clientId = clientId;
    }

    public Set<String> getNames() {
        return names;
    }

    public StatusFilter getStatus() {
        return status;
    }

    public Long getClientId() {return clientId;}

    /**
     * Team Statuses
     */
    public enum StatusFilter {
        ALL, ACTIVE_ONLY, INACTIVE_ONLY;

        /**
         * Create a StatusFilter from a string
         *
         * @param status if the string matches or ALL
         * @return StatusFilter
         */
        public static StatusFilter fromStringOrAll(String status) {
            if (StringUtils.isNotBlank(status)) {
                for (StatusFilter statusFilter : values()) {
                    if (statusFilter.toString().equals(status.toUpperCase())) {
                        return statusFilter;
                    }
                }
            }
            return ALL;
        }

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
        return "TeamSearchFilter{" +
                "names=" + names +
                ", status=" + status +
                '}';
    }
}