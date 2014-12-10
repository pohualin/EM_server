package com.emmisolutions.emmimanager.web.rest.model.groups;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Group;
import com.emmisolutions.emmimanager.model.GroupSearchFilter;
import com.emmisolutions.emmimanager.web.rest.model.PagedResource;
import com.emmisolutions.emmimanager.web.rest.resource.GroupsResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A HATEOAS wrapper for a page of GroupResource objects.
 */
@XmlRootElement(name = "group-page")
public class GroupPage extends PagedResource<GroupResource> {

    @XmlElement(name = "filter")
    private GroupSearchFilter searchFilter;

    public GroupPage() {
    }

    /**
     * Wrapper for group resource objects
     *
     * @param groupResourceSupports to be wrapped
     * @param groupPage             true page
     * @param filter                 which caused the response
     */
    public GroupPage(PagedResources<GroupResource> groupResourceSupports, Page<Group> groupPage, GroupSearchFilter filter) {
        pageDefaults(groupResourceSupports, groupPage);
        addFilterToLinks(filter);
    }

    /**
     * Create the search link
     *
     * @param client to use
     * @return Link for group searches
     * @see com.emmisolutions.emmimanager.web.rest.resource.GroupsResource#list(org.springframework.data.domain.Pageable, org.springframework.data.domain.Sort, String, org.springframework.data.web.PagedResourcesAssembler, String...)
     */
    public static Link createFullSearchLink(Client client) {
        Link link = linkTo(methodOn(GroupsResource.class).listGroupsByClientID(null, null, null, client.getId())).withRel("groups");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                        new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)
                        ));
        return new Link(uriTemplate, link.getRel());
    }


    private void addFilterToLinks(GroupSearchFilter filter) {
        this.searchFilter = filter;
    }

}
