package com.emmisolutions.emmimanager.web.rest.admin.model.client.note;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import com.emmisolutions.emmimanager.model.ClientNote;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * HATEOAS wrapper for ClientNote, essentially a DTO instead of a wrapper.
 */
@XmlRootElement(name = "client-note")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClientNoteResource extends ResourceSupport {

    private ClientNote entity;

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

    public ClientNote getEntity() {
        return entity;
    }

    public void setEntity(ClientNote entity) {
        this.entity = entity;
    }
}
