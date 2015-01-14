package com.emmisolutions.emmimanager.web.rest.admin.model.groups;

import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * A HATEOAS wrapper for a ReferenceTag entity.
 */
@XmlRootElement(name = "reference-tag")
public class ReferenceTagResource extends ResourceSupport {

	private ReferenceTag entity;

	public ReferenceTag getEntity() {
		return entity;
	}

	public void setEntity(ReferenceTag entity) {
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
