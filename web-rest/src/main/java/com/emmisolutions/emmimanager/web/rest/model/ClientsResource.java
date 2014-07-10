package com.emmisolutions.emmimanager.web.rest.model;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.web.rest.jax_rs.ClientsEndpoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * HATEOAS wrapper for paginated Client list.
 */
@XmlRootElement(name = "pages")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClientsResource {

    @XmlElement(name = "client")
    @XmlElementWrapper(name = "page")
    List<ClientResource> content;

    @XmlElement(name = "link")
    private List<Link> links;

    private long totalNumber;

    private String currentPageLinkRel;

    @XmlTransient
    private String basePath;

    // Required for serialization outbound
    public ClientsResource() {
    }

    public ClientsResource(Page<Client> clientPage,
                           String basePath) {
        this.basePath = basePath;
        this.totalNumber = clientPage.getTotalElements();
        this.currentPageLinkRel = "page-" + clientPage.getNumber();
        setLinks(clientPage);
        register(clientPage.getContent());
    }

    private void register(List<Client> entities) {
        this.content = new ArrayList<>();
        if (entities != null) {
            for (Client entity : entities) {
                content.add(new ClientResource(entity, basePath));
            }
        }
    }

    private void setLinks(Page<Client> clientPage) {
        links = new ArrayList<>();
        if (clientPage.hasPrevious()) {
            Pageable previous = clientPage.previousPageable();
            links.add(linkToList("page-previous", previous.getPageNumber(), previous.getPageSize(), previous.getSort()));
        }
        links.add(linkToList("page-first", 0, clientPage.getSize(), clientPage.getSort()));
        if (clientPage.hasNext()) {
            Pageable next = clientPage.nextPageable();
            links.add(linkToList("page-next", next.getPageNumber(), next.getPageSize(), next.getSort()));
        }
        links.add(linkToList("page-last", clientPage.getTotalPages(), clientPage.getSize(), clientPage.getSort()));
        links.addAll(allPages(clientPage));

        // self link has RFC6570 search/filter replacements
        links.add(searchLink(basePath));

    }

    public static Link searchLink(String basePath) {
        return new Link("self", UriBuilder.fromPath(basePath).path(ClientsEndpoint.class, "list"), "{?name,status}");
    }

    private List<Link> allPages(Page<Client> page) {
        List<Link> pageLinks = new ArrayList<>();
        if (page.hasNext()) {
            populatePageLink(page.nextPageable().first(), pageLinks, page.getTotalPages());
        } else if (page.hasPrevious()) {
            populatePageLink(page.previousPageable().first(), pageLinks, page.getTotalPages());
        }
        return pageLinks;
    }

    private void populatePageLink(Pageable page, List<Link> pageLinks, Integer totalPages) {
        links.add(
                linkToList("page-" + page.getPageNumber(), page.getPageNumber(), page.getPageSize(), page.getSort())
        );
        page = page.next();
        if (page.getPageNumber() <= totalPages) {
            populatePageLink(page, pageLinks, totalPages);
        }
    }

    private Link linkToList(String rel, Integer page, Integer max, Sort sort) {
        UriBuilder uriBuilder = UriBuilder.fromPath(basePath)
                .path(ClientsEndpoint.class, "list")
                .queryParam("page", page)
                .queryParam("max", max);
        if (sort != null) {
            StringBuilder sb = new StringBuilder();
            for (Sort.Order order : sort) {
                sb.append(order.getProperty())
                        .append(":")
                        .append(order.getDirection().toString().toLowerCase())
                        .append(";");
            }
            uriBuilder = uriBuilder.queryParam("sort", sb.toString());
        }
        return new Link(javax.ws.rs.core.Link.fromUriBuilder(uriBuilder).rel(rel).build());
    }

}
