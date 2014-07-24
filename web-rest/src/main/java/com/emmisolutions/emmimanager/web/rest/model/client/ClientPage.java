package com.emmisolutions.emmimanager.web.rest.model.client;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientSearchFilter;
import com.emmisolutions.emmimanager.web.rest.resource.ClientsResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * This class encapsulates a 'page' of clients. We need the class due to the way JAXB works.
 * The @XmlSeeAlso is where we help JAXB 'see' the class types in the output.
 */
@XmlSeeAlso({Resource.class, Client.class, ClientResource.class})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "client-search-result")
@XmlType(propOrder = {"filter", "metadata", "links", "content"})
public class ClientPage {

    public ClientPage() {
    }

    @XmlElement(name = "page")
    private PagedResources.PageMetadata metadata;

    @XmlElement(name = "filter")
    private ClientSearchFilter filter;

    @XmlElement(name = "link", namespace = Link.ATOM_NAMESPACE)
    @XmlElementWrapper(name = "links")
    private List<Link> links;

    @XmlElement(name = "content")
    @XmlElementWrapper(name = "contents")
    private Collection<ClientResource> content;

    public ClientPage(PagedResources<ClientResource> clientResourceSupports, Page<Client> clientPage,  ClientSearchFilter filter) {
        this.content = clientResourceSupports.getContent();
        this.metadata = clientResourceSupports.getMetadata();
        this.filter = filter;
        addFilterToLinks(clientResourceSupports.getLinks(), filter);
    }

    public static Link createFullSearchLink(){
        Link link = linkTo(methodOn(ClientsResource.class).list(null, null, null, null, null)).withRel("clients");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                        new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("name", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("status", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
        return new Link(uriTemplate, link.getRel());
    }

    public static Link createReferenceDataLink(){
        return linkTo(methodOn(ClientsResource.class).getReferenceData()).withRel("clientsReferenceData");
    }

    private void addFilterToLinks(List<Link> links, ClientSearchFilter filter) {
        if (CollectionUtils.isEmpty(links)) {
            return;
        }
        this.links = new ArrayList<>();
        for (Link link : links) {
            String rel = link.getRel();
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(link.getHref());
            if (link.isTemplated()) {
                // add args to template
                UriTemplate uriTemplate = new UriTemplate(link.getHref())
                        .with(new TemplateVariables(
                                new TemplateVariable("name", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                                new TemplateVariable("status", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
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
                this.links.add(new Link(builder.build().encode().toUriString(), rel));
            }
        }
    }
}
