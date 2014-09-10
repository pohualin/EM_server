package com.emmisolutions.emmimanager.web.rest.model.client;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import com.emmisolutions.emmimanager.model.ClientRegion;
import com.emmisolutions.emmimanager.model.ClientSearchFilter;
import com.emmisolutions.emmimanager.model.ClientTier;
import com.emmisolutions.emmimanager.model.ClientType;
import com.emmisolutions.emmimanager.web.rest.model.salesforce.SalesForceSearchResponseResource;
import com.emmisolutions.emmimanager.web.rest.model.user.UserPage;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Reference data for client editing
 */
@XmlRootElement(name = "reference-data")
public class ReferenceData extends ResourceSupport {

    @XmlElement(name = "clientType")
    @XmlElementWrapper(name = "clientTypes")
    private ClientType[] clientTypes = ClientType.values();

    @XmlElement(name = "clientRegion")
    @XmlElementWrapper(name = "clientRegions")
    private ClientRegion[] clientRegions = ClientRegion.values();

    @XmlElement(name = "clientTier")
    @XmlElementWrapper(name = "clientTiers")
    private ClientTier[] clientTiers = ClientTier.values();

    @XmlElement(name = "statusFilter")
    @XmlElementWrapper(name = "statusFilters")
    private ClientSearchFilter.StatusFilter[] statusFilters = ClientSearchFilter.StatusFilter.values();
    
    /**
     * create common links for reference data
     */
    public ReferenceData() {
        add(UserPage.createPotentialOwnersFullSearchLink());
        add(SalesForceSearchResponseResource.createFindLink());
        add(ClientResource.createFindNormalizedNameLink());
    }

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
