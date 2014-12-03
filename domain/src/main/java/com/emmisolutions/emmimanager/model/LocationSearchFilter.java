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
 * Filter used for Location searching.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class LocationSearchFilter {
    @XmlElement(name = "name")
    @XmlElementWrapper(name = "names")
    private Set<String> names;

    private StatusFilter status;

    private Client notUsingThisClient;

    private Client belongsToClient;

    /**
     * Constructor
     */
    public LocationSearchFilter() {
        status = StatusFilter.ALL;
    }

    /**
     * Constructor
     *
     * @param names to filter
     */
    public LocationSearchFilter(String... names) {
        this(StatusFilter.ALL, names);
    }

    /**
     * Constructor
     *
     * @param status filter
     * @param names  to filter
     */
    public LocationSearchFilter(StatusFilter status, String... names) {
        if (names != null) {
            this.names = new HashSet<>();
            Collections.addAll(this.names, names);
        }
        if (status != null) {
            this.status = status;
        }
    }

    public Set<String> getNames() {
        return names;
    }

    public StatusFilter getStatus() {
        return status;
    }

    public Client getNotUsingThisClient() {
        return notUsingThisClient;
    }

    public void setNotUsingThisClient(Client notUsingThisClient) {
        this.notUsingThisClient = notUsingThisClient;
    }

    public Client getBelongsToClient() {
        return belongsToClient;
    }

    public void setBelongsToClient(Client belongsToClient) {
        this.belongsToClient = belongsToClient;
    }


    /**
     * Location Statuses
     */
    public enum StatusFilter {
        ALL, ACTIVE_ONLY, INACTIVE_ONLY;

        /**
         * Create a StatusFilter from a string
         *
         * @param status to convert
         * @return one of the enums or ALL if nothing matches
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
        return "LocationSearchFilter{" +
                "names=" + names +
                ", status=" + status +
                '}';
    }
}
