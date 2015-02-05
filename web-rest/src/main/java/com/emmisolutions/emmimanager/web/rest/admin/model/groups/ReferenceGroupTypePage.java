package com.emmisolutions.emmimanager.web.rest.admin.model.groups;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;

import com.emmisolutions.emmimanager.model.ReferenceGroupType;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.ReferenceGroupsResource;

/**
 * A HATEOAS wrapper for a page of ReferenceGroupTypeResource objects.
 */
@XmlRootElement(name = "ref-group-type-page")
public class ReferenceGroupTypePage extends PagedResource<ReferenceGroupTypeResource> {

    public ReferenceGroupTypePage() {
    }

    /**
     * Wrapper for ReferenceGroupResource objects
     *
     * @param groupTypeResourceSupports to be wrapped
     * @param groupTypePage             true page
     */
    public ReferenceGroupTypePage(PagedResources<ReferenceGroupTypeResource> groupTypeResourceSupports, Page<ReferenceGroupType> groupTypePage) {
        pageDefaults(groupTypeResourceSupports, groupTypePage);
    }

    /**
     * Link for ref data
     *
     * @return Link reference data for reference group types
     * @see com.emmisolutions.emmimanager.web.rest.admin.resource.ReferenceGroupsResource#getAllReferenceGroupTypes(org.springframework.data.domain.Pageable, org.springframework.data.domain.Sort, org.springframework.data.web.PagedResourcesAssembler, Long)
     */

    public static Link createRefGroupTypesLink() {
        Link link = linkTo(methodOn(ReferenceGroupsResource.class).getAllReferenceGroupTypes(null, null, null)).withRel("refDataGroupTypes");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
            .with(new TemplateVariables(
                new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)
            ));
        return new Link(uriTemplate, link.getRel());
    }
}
