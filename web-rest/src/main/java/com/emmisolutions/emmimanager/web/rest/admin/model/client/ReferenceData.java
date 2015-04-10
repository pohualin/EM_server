package com.emmisolutions.emmimanager.web.rest.admin.model.client;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.emmisolutions.emmimanager.model.*;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import com.emmisolutions.emmimanager.web.rest.admin.model.groups.ReferenceGroupPage;
import com.emmisolutions.emmimanager.web.rest.admin.model.salesforce.SalesForceSearchResponseResource;
import com.emmisolutions.emmimanager.web.rest.admin.model.user.UserPage;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Reference data for client editing
 */
@XmlRootElement(name = "reference-data")
public class ReferenceData extends ResourceSupport {

    @XmlElement(name = "clientType")
    @XmlElementWrapper(name = "clientTypes")
    private Set<ClientType> clientTypes;

    @XmlElement(name = "clientRegion")
    @XmlElementWrapper(name = "clientRegions")
    private Set<ClientRegion> clientRegions;

    @XmlElement(name = "clientTier")
    @XmlElementWrapper(name = "clientTiers")
    private Set<ClientTier> clientTiers;

    @XmlElement(name = "statusFilter")
    @XmlElementWrapper(name = "statusFilters")
    private ClientSearchFilter.StatusFilter[] statusFilters = ClientSearchFilter.StatusFilter.values();

    /**
     * create common links for reference data
     *
     * @param clientRegions regions
     * @param clientTiers   tiers
     * @param clientTypes   types
     */
    public ReferenceData(Collection<ClientType> clientTypes, Collection<ClientRegion> clientRegions, Collection<ClientTier> clientTiers) {
        this.clientTypes = new HashSet<>(clientTypes);
        this.clientRegions = new HashSet<>(clientRegions);
        this.clientTiers = new HashSet<>(clientTiers);
        add(UserPage.createPotentialOwnersFullSearchLink());
        add(SalesForceSearchResponseResource.createFindLink());
        add(ClientResource.createFindNormalizedNameLink());
        add(ReferenceGroupPage.createGroupReferenceDataLink());
        add(ReferenceGroupPage.createActiveReferenceGroupsDataLink());
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
