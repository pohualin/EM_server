package com.emmisolutions.emmimanager.web.rest.model.user.client;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * HATEOAS wrapper for UserClient, essentially a DTO instead of a wrapper.
 */
@XmlRootElement(name = "user-client")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserClientResource extends ResourceSupport {

	private UserClient entity;

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

	public UserClient getEntity() {
		return entity;
	}

	public void setEntity(UserClient entity) {
		this.entity = entity;
	}

}
