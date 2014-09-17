package com.emmisolutions.emmimanager.web.rest.model.groups;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A HATEOAS wrapper for a ReferenceGroup entity.
 */
@XmlRootElement(name = "reference-group")
public class ReferenceGroupResource extends ResourceSupport {

	private ReferenceGroup entity;

	public ReferenceGroup getEntity() {
		return entity;
	}

	public void setEntity(ReferenceGroup entity) {
		this.entity = entity;
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
