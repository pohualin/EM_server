package com.emmisolutions.emmimanager.web.rest.admin.model.configuration.contentSubscription;

import com.emmisolutions.emmimanager.model.configuration.ClientProgramContentInclusion;
import com.emmisolutions.emmimanager.web.rest.client.model.PagedResource;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A HATEOAS wrapper for a page of ClientProgramContentInclusionResource objects.
 */
@XmlRootElement(name = "client-program-content-inclusion-page")
public class ClientProgramContentInclusionPage extends
        PagedResource<ClientProgramContentInclusionResource> {

    public ClientProgramContentInclusionPage() {
    }

    /**
     * Wrapped constructor
     * 
     * @param clientProgramContentInclusionResources
     *            to be wrapped
     * @param clientProgramContentInclusionPage
     *            the raw response
     */
    public ClientProgramContentInclusionPage(
            PagedResources<ClientProgramContentInclusionResource> clientProgramContentInclusionResources,
            Page<ClientProgramContentInclusion> clientProgramContentInclusionPage) {
        pageDefaults(clientProgramContentInclusionResources,
        		clientProgramContentInclusionPage);
    }

}
