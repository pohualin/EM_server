package com.emmisolutions.emmimanager.model;

import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * The search filter for Client entities
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "filter")
public class ClientSearchFilter {

    @XmlElement(name = "name")
    @XmlElementWrapper(name= "names")
    private Set<String> names;

    private StatusFilter status;

    /**
     * constructor
     */
    public ClientSearchFilter(){
          this.status = StatusFilter.ALL;
    }

    /**
     * all status plus passed names
     * @param names filter
     */
    public ClientSearchFilter(String... names){
        this(StatusFilter.ALL, names);
    }

    /**
     * constructor
     * @param status to filter
     * @param names to filter
     */
    public ClientSearchFilter(StatusFilter status, String... names){
        if (names != null) {
            this.names = new HashSet<>();
            Collections.addAll(this.getNames(), names);
        }
        if ( status != null) {
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
     * Status allowed
     */
    public enum StatusFilter {
        ALL, ACTIVE_ONLY, INACTIVE_ONLY;

        /**
         * from string or ACTIVE
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
        return "ClientSearchFilter{" +
                "names=" + names +
                ", status=" + status +
                '}';
    }
}
