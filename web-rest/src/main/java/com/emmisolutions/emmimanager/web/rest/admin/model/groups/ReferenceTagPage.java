package com.emmisolutions.emmimanager.web.rest.admin.model.groups;

import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;

import javax.xml.bind.annotation.XmlRootElement;

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
}
