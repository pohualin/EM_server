package com.emmisolutions.emmimanager.web.rest.model.provider;

import com.emmisolutions.emmimanager.model.ClientProvider;
import com.emmisolutions.emmimanager.web.rest.model.client.ClientResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * A HATEOAS wrapper for a ClientProvider entity.
 */
@XmlRootElement(name = "providerClient")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProviderClientResource extends ResourceSupport {

    private ClientResource client;


    private String externalId;

    private Long id;

    private Integer version;

    public ProviderClientResource() {
    }

    /**
     * Constructor
     *
     * @param entity         to be wrapped
     * @param clientResource the client
     */
    public ProviderClientResource(ClientProvider entity, ClientResource clientResource) {
        this.externalId = entity.getExternalId();
        this.id = entity.getId();
        this.version = entity.getVersion();
        this.client = clientResource;
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
