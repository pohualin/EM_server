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

    public LocationSearchFilter() {
        status = StatusFilter.ALL;
    }

    public LocationSearchFilter(String... names) {
        this(StatusFilter.ALL, names);
    }

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

    /**
     * Location Statuses
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
    }
}
