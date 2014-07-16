package com.emmisolutions.emmimanager.web.rest.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * We use this Link instead of the javax.ws.rs.core.Link so that we can control the
 * serialization more easily (things like adding the templated attribute) as well as
 * to allow for for non RFC 2396 compliant links (required for javax.ws.rs.core.Link via URI).
 * We especially need to support links specified by RFC 6570: URI Templates
 * E.g. http://example.com/search{?q,lang}
 * <p/>
 * <a href="http://tools.ietf.org/html/rfc6570">RFC 6570: URI Template</a>
 * <p/>
 * <a href="http://www.ietf.org/rfc/rfc2396.txt"><i>RFC&nbsp;2396: Uniform
 * Resource Identifiers (URI): Generic Syntax</i></a>
 */
@XmlRootElement(name = "link")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Link {

    @XmlAttribute(name = "href")
    private String href;
    @XmlAttribute(name = "rel")
    private String rel;
    @XmlAttribute(name = "templated")
    private Boolean templated;
    @XmlAttribute(name = "order")
    private Integer order;

    public Link() {
    }

    public Link(String rel, Integer order, String link, String... rfc6570Parts) {
        this.rel = rel;
        this.order = order;
        StringBuilder sb = new StringBuilder(link);
        for (String s : rfc6570Parts) {
            if (StringUtils.isNotBlank(s)) {
                templated = true;
                sb.append(s);
            }
        }
        this.href = sb.toString();
    }

    public Link(org.springframework.hateoas.Link link) {
        this(link, null);
    }

    public Link(org.springframework.hateoas.Link link, Integer order) {
        this(link.getRel(), order, link.getHref(), "");
    }

}
