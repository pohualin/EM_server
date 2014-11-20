package com.emmisolutions.emmimanager.web.rest.model.clientlocation;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;

import com.emmisolutions.emmimanager.model.ClientLocation;
import com.emmisolutions.emmimanager.web.rest.model.PagedResource;

/**
 * A HATEOAS wrapper for a page of ClientLocationResource objects.
 */
@XmlRootElement(name = "location-client-page")
public class LocationClientResourcePage extends
		PagedResource<ClientLocationResource> {

	public LocationClientResourcePage() {
	}

	/**
	 * Constructor that sets up paging defaults
	 *
	 * @param clientLocationResourceSupports
	 *            page of ClientLocationResource objects
	 * @param clientLocationPage
	 *            page of ClientLocation objects
	 */
	public LocationClientResourcePage(
			PagedResources<ClientLocationResource> clientLocationResourceSupports,
			Page<ClientLocation> clientLocationPage) {
		pageDefaults(clientLocationResourceSupports, clientLocationPage);
	}

}
