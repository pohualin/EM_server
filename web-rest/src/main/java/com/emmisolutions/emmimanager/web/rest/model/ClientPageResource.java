package com.emmisolutions.emmimanager.web.rest.model;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.web.rest.spring.ClientsResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * HATEOAS wrapper for paginated Client list.
 */
@XmlRootElement(name = "pages")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClientPageResource {

    @XmlElement(name = "client")
    @XmlElementWrapper(name = "page")
    List<ClientResource> content;

    @XmlElementWrapper(name = "navigation-links")
    @XmlElement(name = "navigation-link")
    private List<Link> links;

    @XmlElement(name = "self-link")
    private Link selfLink;

    @XmlElement(name = "load-link")
    private Link loadLink;

    @XmlElement(name = "create-link")
    private Link newLink;

    private long totalNumber;

    private int currentPage;

    private int totalPages;

    private int pageSize;

    // Required for serialization outbound
    public ClientPageResource() {
    }

    public ClientPageResource(Page<Client> clientPage) {
        this.totalNumber = clientPage.getTotalElements();
        this.currentPage = clientPage.getNumber();
        this.totalPages = clientPage.getTotalPages();
        this.pageSize = clientPage.getSize();
        setLinks(clientPage);
        register(clientPage.getContent());
    }

    private void register(List<Client> entities) {
        this.content = new ArrayList<>();
        if (entities != null) {
            for (Client entity : entities) {
                content.add(new ClientResource(entity));
            }
        }
    }

    private void setLinks(Page<Client> clientPage) {
        links = new ArrayList<>();

        if (clientPage.hasPrevious()) {
            links.add(linkToList("page-first", 0, clientPage.getSize(), clientPage.getSort(), 0));
            Pageable previous = clientPage.previousPageable();
            links.add(linkToList("page-previous", previous.getPageNumber(), previous.getPageSize(), previous.getSort(), 1));
        }
        links.addAll(allPages(clientPage));
        if (clientPage.hasNext()) {
            Pageable next = clientPage.nextPageable();
            links.add(linkToList("page-next", next.getPageNumber(), next.getPageSize(), next.getSort(), links.size()));
            links.add(linkToList("page-last", clientPage.getTotalPages() - 1, clientPage.getSize(), clientPage.getSort(), links.size()));
        }

        // load link has RFC6570 search/filter replacements
        this.loadLink = searchLink();

        this.selfLink = linkToList("self", clientPage.getNumber(), clientPage.getSize(), clientPage.getSort(), null);

        this.newLink = createLink();
    }

    public static Link createLink() {
        return new Link(linkTo(methodOn(ClientsResource.class).create(new Client())).withRel("createClient"));
    }

    public static Link searchLink() {
        return new Link("listClients", null, linkTo(methodOn(ClientsResource.class).list(null, null, null, null, null)).withRel("list").getHref(), "{?name,status,max,sort}");
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
                linkToList("page-" + page.getPageNumber(), page.getPageNumber(), page.getPageSize(), page.getSort(), page.getPageNumber() + 1)
        );
        page = page.next();
        if (page.getPageNumber() < totalPages) {
            populatePageLink(page, pageLinks, totalPages);
        }
    }

    private Link linkToList(String rel, Integer page, Integer max, Sort sort, Integer linkOrder) {
        StringBuilder sb = new StringBuilder();
        if (sort != null) {
            for (Sort.Order sortOrder : sort) {
                sb.append(sortOrder.getProperty())
                        .append(":")
                        .append(sortOrder.getDirection().toString().toLowerCase())
                        .append(";");
            }
        }
        return new Link(linkTo(methodOn(ClientsResource.class).list(page, max, sb.toString(), null, null)).withRel(rel), linkOrder);
    }

}