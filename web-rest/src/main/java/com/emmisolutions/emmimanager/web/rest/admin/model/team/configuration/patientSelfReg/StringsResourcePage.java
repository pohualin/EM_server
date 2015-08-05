package com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.patientSelfReg;

import com.emmisolutions.emmimanager.model.Strings;
import com.emmisolutions.emmimanager.web.rest.client.model.PagedResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;

/**
 * A page of strings resources
 */
public class StringsResourcePage extends PagedResource<StringsResource> {

    /**
     * Creates a page of StringsResource objects for serialization to
     * the front
     *
     * @param stringsResources
     * @param stringsPage
     */
    public StringsResourcePage(PagedResources<StringsResource> stringsResources,
                               Page<Strings> stringsPage) {
        pageDefaults(stringsResources, stringsPage);
    }

    public StringsResourcePage() {
    }

}
