package com.emmisolutions.emmimanager.web.rest.admin.model.salesforce;

import com.emmisolutions.emmimanager.model.SalesForceSearchResponse;
import com.emmisolutions.emmimanager.web.rest.admin.resource.SalesForceResource;
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

    /**
     * Construct response from entity and query
     *
     * @param entity     the wrapped entity
     * @param query      the query that caused the response
     * @param teamSearch is it a team search or not
     */
    public SalesForceSearchResponseResource(SalesForceSearchResponse entity, String query, boolean teamSearch) {
        this.query = query;
        this.entity = entity;
        if (!teamSearch) {
            add(linkTo(methodOn(SalesForceResource.class).find(query)).withSelfRel());
        } else {
            add(linkTo(methodOn(SalesForceResource.class).findForTeam(query)).withSelfRel());
        }
    }

    /**
     * The link to find all sf accounts from client
     *
     * @return the link
     */
    public static Link createFindLink() {
        Link link = linkTo(methodOn(SalesForceResource.class).find(null)).withRel("findSalesForceAccount");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
            .with(new TemplateVariables(
                new TemplateVariable("q", TemplateVariable.VariableType.REQUEST_PARAM)));
        return new Link(uriTemplate, link.getRel());
    }

    /**
     * The link to find all sf accounts from team
     *
     * @return the link
     */
    public static Link createFindTeamLink() {
        Link link = linkTo(methodOn(SalesForceResource.class).findForTeam(null)).withRel("findTeamSalesForceAccount");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
            .with(new TemplateVariables(
                new TemplateVariable("q", TemplateVariable.VariableType.REQUEST_PARAM)));
        return new Link(uriTemplate, link.getRel());
    }

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

}
