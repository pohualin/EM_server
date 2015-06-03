package com.emmisolutions.emmimanager.web.rest.admin.model.user.client;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.hateoas.ResourceSupport;

import com.emmisolutions.emmimanager.model.UserClientCommonSearchFilter;

/**
 * Reference data for client editing
 */
@XmlRootElement(name = "reference-data")
public class ReferenceData extends ResourceSupport {

    @XmlElement(name = "statusFilter")
    @XmlElementWrapper(name = "statusFilters")
    private UserClientCommonSearchFilter.StatusFilter[] statusFilters = UserClientCommonSearchFilter.StatusFilter
            .values();

}
