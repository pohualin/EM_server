package com.emmisolutions.emmimanager.web.rest.model.client;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;

import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.model.TagSearchFilter;
import com.emmisolutions.emmimanager.web.rest.model.PagedResource;
import com.emmisolutions.emmimanager.web.rest.resource.TagsResource;

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
     * @param filter                 which caused the response
     */

    public TagPage(PagedResources<TagResource> tagResourceSupports, Page<Tag> tagPage, TagSearchFilter filter) {
        pageDefaults(tagResourceSupports, tagPage);
        addFilterToLinks(filter);
    }


    /**
     * Create the search link
     *
     * @return Link for group searches
     * @see com.emmisolutions.emmimanager.web.rest.resource.GroupsResource#list(org.springframework.data.domain.Pageable, org.springframework.data.domain.Sort, String, org.springframework.data.web.PagedResourcesAssembler, String...)
     */
    public static Link createFullSearchLink() {
        Link link = linkTo(methodOn(TagsResource.class).listTagsByGroupID(null, null, null, (Long) null)).withRel("tag");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                        new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("name", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)
                        ));
        return new Link(uriTemplate, link.getRel());
    }
    

    private void addFilterToLinks(TagSearchFilter filter) {
        this.searchFilter = filter;
    }

    
}
