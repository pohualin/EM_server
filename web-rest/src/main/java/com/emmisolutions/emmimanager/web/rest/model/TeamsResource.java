package com.emmisolutions.emmimanager.web.rest.model;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.web.rest.endpoint.TeamsEndpoint;

import javax.ws.rs.core.Link;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * HATEOAS wrapper for pages of team objects.
 */
@XmlType(name = "entities")
@XmlRootElement(name = "pages")
@XmlAccessorType(XmlAccessType.FIELD)
public class TeamsResource {

    @XmlElement(name = "team")
    @XmlElementWrapper(name = "page")
    List<TeamResource> content;

    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    private List<Link> links;

    @XmlTransient
    private String basePath;

    private Integer totalNumber;

    public TeamsResource() {
    }

    public TeamsResource(Client client,
                         List<Team> entities,
                         Integer page,
                         Integer pageSize,
                         Integer lastPage,
                         Integer totalNumber,
                         String basePath) {
        this.basePath = basePath;
        this.totalNumber = totalNumber;
        register(entities);
        pageLinks(client, page, pageSize, lastPage);
    }

    private void register(List<Team> entities) {
        this.content = new ArrayList<>();
        if (entities != null) {
            for (Team entity : entities) {
                content.add(new TeamResource(entity, basePath));
            }
        }
    }

    private void pageLinks(Client client, Integer page, Integer max, Integer lastPage) {
        this.links = new ArrayList<>();
        links.add(linkToListByClient("first", client, 0, max));
        links.add(linkToListByClient("next", client, page + 1, max));
        links.add(linkToListByClient("last", client, lastPage, max));
    }

    private Link linkToListByClient(String rel, Client client, Integer page, Integer max) {
        return Link.fromUriBuilder(toListByClientMethod(page, max))
                .rel(rel)
                .build(client.getId());
    }

    private UriBuilder toListByClientMethod(Integer page, Integer max) {
        return UriBuilder.fromPath(basePath)
                .path(TeamsEndpoint.class, "listByClient")
                .queryParam("page", page)
                .queryParam("max", max);
    }
}
