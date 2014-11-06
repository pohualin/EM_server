package com.emmisolutions.emmimanager.web.rest.model.clientprovider;

import com.emmisolutions.emmimanager.model.ClientProvider;
import com.emmisolutions.emmimanager.web.rest.model.provider.ProviderResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * A HATEOAS wrapper for a ClientProvider entity.
 */
@XmlRootElement(name = "clientProvider")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClientProviderResource extends ResourceSupport {

    private ProviderResource provider;


    private String externalId;

    private Long id;

    private Integer version;

    public ClientProviderResource() {
    }

    public ClientProviderResource(ClientProvider entity, ProviderResource providerResource) {
        this.externalId = entity.getExternalId();
        this.id = entity.getId();
        this.version = entity.getVersion();
        this.provider = providerResource;
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
