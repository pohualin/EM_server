package com.emmisolutions.emmimanager.web.rest.model.client;

import com.emmisolutions.emmimanager.model.ClientRegion;
import com.emmisolutions.emmimanager.model.ClientSearchFilter;
import com.emmisolutions.emmimanager.model.ClientTier;
import com.emmisolutions.emmimanager.model.ClientType;
import com.emmisolutions.emmimanager.web.rest.model.user.UserPage;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Reference data for client editing
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class ReferenceData extends ResourceSupport{

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

    public ReferenceData(){
        add(UserPage.createPotentialOwnersFullSearchLink());
    }

    @XmlElement(name = "link", namespace = Link.ATOM_NAMESPACE)
    @XmlElementWrapper(name = "links")
    public List<Link> getLinks(){
        return super.getLinks();
    }

}
