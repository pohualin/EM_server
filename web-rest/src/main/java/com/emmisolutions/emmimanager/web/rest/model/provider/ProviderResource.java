package com.emmisolutions.emmimanager.web.rest.model.provider;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.hateoas.ResourceSupport;

import com.emmisolutions.emmimanager.model.Provider;

@XmlRootElement(name = "provider")
public class ProviderResource extends ResourceSupport {

	private Provider entity;

	public Provider getEntity() {
		return entity;
	}

	public void setEntity(Provider entity) {
		this.entity = entity;
	}

}
