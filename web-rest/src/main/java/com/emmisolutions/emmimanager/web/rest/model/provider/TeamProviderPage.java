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
import com.emmisolutions.emmimanager.web.rest.resource.TeamProvidersResource;

/**
 * A HATEOAS wrapper for a page of TeamProviderResource objects.
 */
@XmlRootElement(name = "team-provider-page")
public class TeamProviderPage extends PagedResource<TeamProviderResource> {

	public TeamProviderPage() {
	}

	/**
	 * Wrapped constructor
	 *
	 * @param providerResources  to be wrapped
	 * @param providerPage  the raw response
	 */
	public TeamProviderPage(PagedResources<TeamProviderResource> providerResources, Page<TeamProvider> providerPage) {
		pageDefaults(providerResources, providerPage);
	}

	/**
	 * Link to get TeamProvider by id
	 * 
	 * @param teamProviderId
	 * 
	 * @return Link for get teamProvider by id
	 */

	public static Link createProviderByIdLink(Long teamProviderId) {
		Link link = linkTo(methodOn(TeamProvidersResource.class).getById(teamProviderId)).withRel("findProviderById");
		UriTemplate uriTemplate = new UriTemplate(link.getHref());
		return new Link(uriTemplate, link.getRel());
	}
}
