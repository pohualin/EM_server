package com.emmisolutions.emmimanager.web.rest.model.provider;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.lang.reflect.Method;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.core.AnnotationMappingDiscoverer;
import org.springframework.hateoas.core.DummyInvocationUtils;
import org.springframework.hateoas.core.MappingDiscoverer;
import org.springframework.web.bind.annotation.RequestMapping;

import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.web.rest.model.PagedResource;
import com.emmisolutions.emmimanager.web.rest.resource.ProvidersResource;

/**
 * A HATEOAS wrapper for a page of ProviderResource objects.
 */
@XmlRootElement(name = "provider-page")
public class ProviderPage extends PagedResource<ProviderResource> {

    private static final MappingDiscoverer discoverer = new AnnotationMappingDiscoverer(RequestMapping.class);

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
     * Link for ref data (Specialty types) for providers
     *
     * @return Link reference data for providers
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
        
    /**
     * Link for Create Provider
     * @param clientId
     * @param teamId
     * 
     * @return Link for create provider
     */
    public static Link createProviderLink(Long clientId, Long teamId) {
        DummyInvocationUtils.LastInvocationAware invocations = (DummyInvocationUtils.LastInvocationAware) methodOn(ProvidersResource.class).create(null, 1L, 1L);
        Method method = invocations.getLastInvocation().getMethod();
        Link link = linkTo(invocations).withRel("providerzz");
        String href = link.getHref();
        int idx = href.indexOf(discoverer.getMapping(ProvidersResource.class));
        if (idx != -1) {
            return new Link(
                    href.substring(0, idx) + discoverer.getMapping(ProvidersResource.class, method).replace("{clientId}", "" + clientId).replace("{teamId}", "" + teamId),
                    link.getRel());
        }
        return null;
    }
    

}
