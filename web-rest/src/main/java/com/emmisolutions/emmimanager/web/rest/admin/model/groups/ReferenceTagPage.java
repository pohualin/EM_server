package com.emmisolutions.emmimanager.web.rest.admin.model.groups;


import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.ReferenceGroupsResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.*;

import javax.xml.bind.annotation.XmlRootElement;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A HATEOAS wrapper for a page of ReferenceTagResource objects.
 */
@XmlRootElement(name = "ref-tag-page")
public class ReferenceTagPage extends PagedResource<ReferenceTagResource> {

    public ReferenceTagPage() {
    }

    /**
     * Wrapper for ReferenceTagResource objects
     *
     * @param tagResourceSupports to be wrapped
     * @param tagPage             true page
     */
    public ReferenceTagPage(PagedResources<ReferenceTagResource> tagResourceSupports, Page<ReferenceTag> tagPage) {
        pageDefaults(tagResourceSupports, tagPage);
    }
    
    public static Link createTagsReferenceDataLink(Long referenceGroupId) {
        Link link = linkTo(methodOn(ReferenceGroupsResource.class).getAllReferenceTagsByGroup(null, null, referenceGroupId)).withRel("refTagsForGroup");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
            .with(new TemplateVariables(
                new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)
            ));
        return new Link(uriTemplate, link.getRel());
    }
}
