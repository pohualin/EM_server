package com.emmisolutions.emmimanager.web.rest.admin.model.user.client;

import com.emmisolutions.emmimanager.model.UserClientSearchFilter;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * A HATEOAS wrapper for a page of UserClientResource objects.
 */
@XmlRootElement(name = "user-client-page")
public class UserClientPage extends PagedResource<UserClientResource> {

    @XmlElement(name = "filter")
    private UserClientSearchFilter filter;

    @XmlElement(name = "statusFilter")
    @XmlElementWrapper(name = "statusFilters")
    private UserClientSearchFilter.StatusFilter[] statusFilters = UserClientSearchFilter.StatusFilter.values();

    public UserClientPage() {
    }

    /**
     * Wrapped constructor
     *
     * @param userResources to be wrapped
     */
    public UserClientPage(PagedResources<UserClientResource> userResources,
                          Page<UserClient> userPage) {
        pageDefaults(userResources, userPage);
    }

    /**
     * Wrapped constructor
     *
     * @param userResources to be wrapped
     * @param userPage      the raw response
     */
    public UserClientPage(PagedResources<UserClientResource> userResources,
                          Page<UserClient> userPage, UserClientSearchFilter filter) {
        pageDefaults(userResources, userPage);
        addFilterToLinks(filter);
    }

    private void addFilterToLinks(UserClientSearchFilter filter) {
        this.filter = filter;
        if (CollectionUtils.isEmpty(links)) {
            return;
        }
        List<Link> existingLinks = links;
        this.links = new ArrayList<>();
        for (Link link : existingLinks) {
            String rel = link.getRel();
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(link.getHref());
            if (link.isTemplated()) {
                // add args to template
                UriTemplate uriTemplate = new UriTemplate(link.getHref())
                        .with(new TemplateVariables(
                                new TemplateVariable(
                                        "term",
                                        TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                                new TemplateVariable(
                                        "status",
                                        TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
                this.links.add(new Link(uriTemplate.toString(), rel));
            } else {
                // add values
                if (filter.getStatus() != null) {
                    builder.queryParam("status", filter.getStatus());
                }
                if (StringUtils.isNotBlank(filter.getTerm())) {
                    builder.queryParam("term", filter.getTerm());
                }
                this.links.add(new Link(builder.build().encode().toUriString(),
                        rel));
            }
        }
    }
}
