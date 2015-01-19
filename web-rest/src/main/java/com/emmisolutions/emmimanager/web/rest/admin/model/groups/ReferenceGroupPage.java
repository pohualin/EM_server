package com.emmisolutions.emmimanager.web.rest.admin.model.groups;

import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.GroupsResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.*;

import javax.xml.bind.annotation.XmlRootElement;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A HATEOAS wrapper for a page of ReferenceGroupResource objects.
 */
@XmlRootElement(name = "ref-group-page")
public class ReferenceGroupPage extends PagedResource<ReferenceGroupResource> {

    public ReferenceGroupPage() {
    }

    /**
     * Wrapper for ReferenceGroupResource objects
     *
     * @param groupResourceSupports to be wrapped
     * @param groupPage             true page
     */
    public ReferenceGroupPage(PagedResources<ReferenceGroupResource> groupResourceSupports, Page<ReferenceGroup> groupPage) {
        pageDefaults(groupResourceSupports, groupPage);
    }

    /**
     * Link for ref data
     *
     * @return Link reference data for groups and tags
     * @see com.emmisolutions.emmimanager.web.rest.admin.resource.GroupsResource#listGroupsByClientID(org.springframework.data.domain.Pageable, org.springframework.data.domain.Sort, org.springframework.data.web.PagedResourcesAssembler, Long)
     */
    public static Link createGroupReferenceDataLink() {
        Link link = linkTo(methodOn(GroupsResource.class).getRefGroups(null, null, null)).withRel("refDataGroups");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
            .with(new TemplateVariables(
                new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)
            ));
        return new Link(uriTemplate, link.getRel());
    }

}
