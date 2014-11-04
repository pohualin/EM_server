package com.emmisolutions.emmimanager.web.rest.model.clientprovider;

import com.emmisolutions.emmimanager.web.rest.model.client.ClientResource;
import com.emmisolutions.emmimanager.web.rest.model.provider.ProviderResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * A HATEOAS wrapper for a ClientProvider entity.
 */
@XmlRootElement(name = "clientProvider")
public class ClientProviderResource extends ResourceSupport{

    private ProviderResource provider;

    private ClientResource client;

    /**
     * Override to change the link property name for serialization
     *
     * @return links
     */
    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @JsonProperty("link")
    public List<Link> getLinks(){
        return super.getLinks();
    }


    public ProviderResource getProvider() {
        return provider;
    }

    public void setProvider(ProviderResource provider) {
        this.provider = provider;
    }

    public ClientResource getClient() {
        return client;
    }

    public void setClient(ClientResource client) {
        this.client = client;
    }
}
