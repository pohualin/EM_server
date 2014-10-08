package com.emmisolutions.emmimanager.web.rest.model.provider;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;

import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.web.rest.model.PagedResource;
import com.emmisolutions.emmimanager.web.rest.resource.GroupsResource;
import com.emmisolutions.emmimanager.web.rest.resource.ProvidersResource;

/**
 * A HATEOAS wrapper for a page of ProviderResource objects.
 */
@XmlRootElement(name = "provider-page")
public class ProviderPage extends PagedResource<ProviderResource> {

    public ProviderPage() {
    }

    /**
     * Wrapped constructor
     *
     * @param providerResources to be wrapped
     * @param providerPage      the raw response
     */
    public ProviderPage(PagedResources<ProviderResource> providerResources, Page<Provider> providerPage) {
        pageDefaults(providerResources, providerPage);
    }

    /**
     * Creates link used to find providers.
     *
     * @return a <link rel="provider" href="http://thelink"/>
     */
    public static Link createsearchProvidersLink() {
        Link link = linkTo(methodOn(ProvidersResource.class).findProviders(null, null, null, null, (String[]) null)).withRel("provider");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
                .with(new TemplateVariables( 
                		new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                        new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("name", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("status", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
        return new Link(uriTemplate, link.getRel());
    }
    
    /**
     * Link for ref data
     *
     * @return Link reference data for groups and tags
     * @see com.emmisolutions.emmimanager.web.rest.resource.GroupsResource#list(org.springframework.data.domain.Pageable, org.springframework.data.domain.Sort, String, org.springframework.data.web.PagedResourcesAssembler, String...)
     */
    public static Link createProviderReferenceDataLink() {
        Link link = linkTo(methodOn(ProvidersResource.class).getRefData(null, null, null)).withRel("providerReferenceData");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                        new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)
                        ));
        return new Link(uriTemplate, link.getRel());
    } 
}
