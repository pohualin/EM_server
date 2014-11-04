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
 * Filter used for Provider searching.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ProviderSearchFilter {
	@XmlElement(name = "name")
    @XmlElementWrapper(name = "names")
    private Set<String> names;

    private StatusFilter status;

    /**
     * Constructor
     */
    public ProviderSearchFilter() {
        status = StatusFilter.ALL;
    }

    /**
     * Constructor
     * @param names to filter
     */
    public ProviderSearchFilter(String... names) {
        this(StatusFilter.ALL, names);
    }

    /**
     * Creates a search filter using status and names
     * @param status the status
     * @param names the names to filter by
     */
    public ProviderSearchFilter(StatusFilter status, String... names) {
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
     * Team Statuses
     */
    public enum StatusFilter {
        ALL, ACTIVE_ONLY, INACTIVE_ONLY;

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
		return "ProviderSearchFilter [names=" + names + ", status=" + status
				+ "]";
	}

}
