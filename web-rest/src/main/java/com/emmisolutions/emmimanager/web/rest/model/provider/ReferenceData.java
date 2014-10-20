package com.emmisolutions.emmimanager.web.rest.model.provider;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import com.emmisolutions.emmimanager.model.ProviderSearchFilter;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Reference data for provider
 */
@XmlRootElement(name = "reference-data")
public class ReferenceData extends ResourceSupport {

    @XmlElement(name = "statusFilter")
    @XmlElementWrapper(name = "statusFilters")
    private ProviderSearchFilter.StatusFilter[] statusFilters = ProviderSearchFilter.StatusFilter.values();
    
    /**
     * Override to change the link property name for serialization
     *
     * @return links
     */
    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @JsonProperty("link")
    public List<Link> getLinks() {
        return super.getLinks();
    }
}
