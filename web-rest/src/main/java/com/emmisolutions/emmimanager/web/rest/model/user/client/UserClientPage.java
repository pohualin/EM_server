package com.emmisolutions.emmimanager.web.rest.model.user.client;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import com.emmisolutions.emmimanager.model.UserClientSearchFilter;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.web.rest.model.PagedResource;

/**
 * A HATEOAS wrapper for a page of UserResource objects.
 */
@XmlRootElement(name = "user-page")
public class UserClientPage extends PagedResource<UserClientResource> {

    private UserClientSearchFilter filter;

    public UserClientPage() {
    }

    /**
     * Wrapped constructor
     *
     * @param userResources
     *            to be wrapped
     * @param userPage
     *            the raw response
     */
    public UserClientPage(PagedResources<UserClientResource> userResources,
	    Page<UserClient> userPage, UserClientSearchFilter filter) {
	pageDefaults(userResources, userPage);
	addFilterToLinks(filter);
    }

    private void addFilterToLinks(UserClientSearchFilter filter) {
	this.filter = filter;
	if (CollectionUtils.isEmpty(links)) {
	    return;
	}
	List<Link> existingLinks = links;
	this.links = new ArrayList<>();
	for (Link link : existingLinks) {
	    String rel = link.getRel();
	    UriComponentsBuilder builder = UriComponentsBuilder
		    .fromHttpUrl(link.getHref());
	    if (link.isTemplated()) {
		// add args to template
		UriTemplate uriTemplate = new UriTemplate(link.getHref())
			.with(new TemplateVariables(
				new TemplateVariable(
					"name",
					TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
				new TemplateVariable(
					"status",
					TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
		this.links.add(new Link(uriTemplate.toString(), rel));
	    } else {
		// add values
		if (filter.getStatus() != null) {
		    builder.queryParam("status", filter.getStatus());
		}
		if (!CollectionUtils.isEmpty(filter.getNames())) {
		    for (String s : filter.getNames()) {
			builder.queryParam("name", s);
		    }
		}
		this.links.add(new Link(builder.build().encode().toUriString(),
			rel));
	    }
	}
    }
}
