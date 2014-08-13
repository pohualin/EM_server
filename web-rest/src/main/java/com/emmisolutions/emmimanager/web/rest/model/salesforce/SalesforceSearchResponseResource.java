package com.emmisolutions.emmimanager.web.rest.model.salesforce;

import com.emmisolutions.emmimanager.model.SalesForceSearchResponse;
import com.emmisolutions.emmimanager.web.rest.resource.SalesForceResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.*;

import javax.xml.bind.annotation.*;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A search response from a sales force search query
 */
@XmlRootElement(name = "sf-search-response")
@XmlAccessorType(XmlAccessType.FIELD)
public class SalesForceSearchResponseResource extends ResourceSupport {

    private String query;

    private SalesForceSearchResponse entity;

    public SalesForceSearchResponseResource() {
    }

    public SalesForceSearchResponseResource(SalesForceSearchResponse entity, String query) {
        this.query = query;
        this.entity = entity;
        add(linkTo(methodOn(SalesForceResource.class).find(query)).withSelfRel());
    }

    public static Link createFindLink() {
        Link link = linkTo(methodOn(SalesForceResource.class).find(null)).withRel("findSalesForceAccount");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("q", TemplateVariable.VariableType.REQUEST_PARAM)));
        return new Link(uriTemplate, link.getRel());
    }

    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @JsonProperty("link")
    public List<Link> getLinks() {
        return super.getLinks();
    }

}
