package com.emmisolutions.emmimanager.web.rest.model.provider;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.UriTemplate;

import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.web.rest.model.PagedResource;
import com.emmisolutions.emmimanager.web.rest.resource.ProvidersResource;

/**
 * A HATEOAS wrapper for a page of ProviderResource objects.
 */
@XmlRootElement(name = "team-provider-page")
public class TeamProviderPage extends PagedResource<TeamProviderResource> {

	public TeamProviderPage() {
	}

	/**
	 * Wrapped constructor
	 *
	 * @param providerResources
	 *            to be wrapped
	 * @param providerPage
	 *            the raw response
	 */
	public TeamProviderPage(PagedResources<TeamProviderResource> providerResources,Page<TeamProvider> providerPage) {
		pageDefaults(providerResources, providerPage);

	}

	/**
	 * Link for Create Provider
	 * 
	 * @param clientId
	 * @param teamId
	 * 
	 * @return Link for create provider
	 */
/*	public static Link createTeamProviderSelfLink(Long id, Long clientId, Long teamId) {
		Link link = linkTo(methodOn(ProvidersResource.class).getById(id, teamId, clientId)).withRel("teamProviderResource");
		UriTemplate uriTemplate = new UriTemplate(link.getHref());
		return new Link(uriTemplate, link.getRel());
	}*/

}
