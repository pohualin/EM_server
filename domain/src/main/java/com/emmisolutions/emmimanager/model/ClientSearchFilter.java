package com.emmisolutions.emmimanager.model;

import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.HashSet;
import java.util.Set;

@XmlAccessorType(XmlAccessType.FIELD)
public class ClientSearchFilter {

    @XmlElement(name = "name")
    @XmlElementWrapper(name= "names")
    private Set<String> names = new HashSet<>();

    private StatusFilter status;

    public ClientSearchFilter(){

    }

    public ClientSearchFilter(Set<String> names, String status){
        this.names = names;
        this.status = StatusFilter.fromStringOrAll(status);
    }

    public Set<String> getNames() {
        return names;
    }

    public void setNames(Set<String> names) {
        this.names = names;
    }

    public StatusFilter getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = StatusFilter.fromStringOrAll(status);
    }

    public enum StatusFilter {
        ALL, ACTIVE_ONLY, INACTIVE_ONLY;

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
