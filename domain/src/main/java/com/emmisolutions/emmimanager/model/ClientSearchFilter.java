package com.emmisolutions.emmimanager.model;

import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "filter")
public class ClientSearchFilter {

    @XmlElement(name = "name")
    @XmlElementWrapper(name= "names")
    private Set<String> names;

    private StatusFilter status;

    public ClientSearchFilter(){
          this.status = StatusFilter.ALL;
    }

    public ClientSearchFilter(String... names){
        this(StatusFilter.ALL, names);
    }

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
