package com.emmisolutions.emmimanager.web.rest.model.groups;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;

import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.emmisolutions.emmimanager.web.rest.model.PagedResource;

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
     * @param PagedResources<ReferenceGroupResource> to be wrapped
     * @param Page<ReferenceGroup>             true page
     * @return ReferenceTagPage
     */
    public ReferenceTagPage(PagedResources<ReferenceTagResource> tagResourceSupports, Page<ReferenceTag> tagPage) {
        pageDefaults(tagResourceSupports, tagPage);
    }
}
