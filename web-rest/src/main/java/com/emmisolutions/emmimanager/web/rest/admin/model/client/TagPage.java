package com.emmisolutions.emmimanager.web.rest.admin.model.client;

import com.emmisolutions.emmimanager.model.Group;
import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.model.TagSearchFilter;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.TagsResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A HATEOAS wrapper for a page of GroupResource objects.
 */
@XmlRootElement(name = "tag-page")
public class TagPage extends PagedResource<TagResource> {

    @XmlElement(name = "filter")
    private TagSearchFilter searchFilter;

    public TagPage() {
    }

    /**
     * Wrapper for tag resource objects
     *
     * @param tagResourceSupports to be wrapped
     * @param tagPage             true page
     * @param filter              which caused the response
     */
    public TagPage(PagedResources<TagResource> tagResourceSupports, Page<Tag> tagPage, TagSearchFilter filter) {
        pageDefaults(tagResourceSupports, tagPage);
        addFilterToLinks(filter);
    }


    /**
     * Create the search link
     *
     * @param group to search for tags
     * @return Link for tag searches
     * @see com.emmisolutions.emmimanager.web.rest.admin.resource.TagsResource#listTagsByGroupID(org.springframework.data.domain.Pageable, org.springframework.data.web.PagedResourcesAssembler, Long)
     */
    public static Link createFullSearchLink(Group group) {
        Link link = linkTo(methodOn(TagsResource.class).listTagsByGroupID(null, null, group.getId())).withRel("tags");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
            .with(new TemplateVariables(
                new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)
            ));
        return new Link(uriTemplate, link.getRel());
    }


    private void addFilterToLinks(TagSearchFilter filter) {
        this.searchFilter = filter;
    }


}
