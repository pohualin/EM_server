package com.emmisolutions.emmimanager.web.rest.client.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.jaxb.OrderAdapter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a base class which defines a response page serialization specification.
 *
 * @param <T> the resource to wrap
 */
@XmlType(propOrder = {"sortOrder", "metadata", "links", "content"})
public abstract class PagedResource<T> {

    @XmlElement(name = "page")
    protected PagedResources.PageMetadata metadata;

    @XmlElement(name = "sort")
    @XmlElementWrapper(name = "sorts")
    @XmlJavaTypeAdapter(value = OrderAdapter.class, type = Sort.Order.class)
    protected List<Sort.Order> sortOrder;

    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    protected List<Link> links;

    @XmlElement(name = "content")
    @XmlElementWrapper(name = "contents")
    protected List<T> content;

    /**
     * Populates instance variables (e.g. content, metadata, links, etc) from the
     * passed arguments
     *
     * @param pagedResources is where we get the content, metadata and links
     * @param page           is where we get the sort and sort order
     */
    public void pageDefaults(PagedResources<T> pagedResources, Page<?> page) {
        if (pagedResources != null) {
            this.content = new ArrayList<>(pagedResources.getContent());
            this.metadata = pagedResources.getMetadata();
            this.links = pagedResources.getLinks();
        }
        if (page != null && page.getSort() != null) {
            sortOrder = new ArrayList<>();
            for (Sort.Order order : page.getSort()) {
                sortOrder.add(order);
            }
        }
    }
}
