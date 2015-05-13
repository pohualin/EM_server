package com.emmisolutions.emmimanager.web.rest.client.model.program.specialty;

import com.emmisolutions.emmimanager.model.program.Specialty;
import com.emmisolutions.emmimanager.web.rest.client.model.PagedResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;

/**
 * A page of specialty resources
 */
public class SpecialtyResourcePage extends PagedResource<SpecialtyResource> {

    /**
     * Creates a page of SpecialtyResource objects for serialization to
     * the front
     *
     * @param specialtyResources the paged resources
     * @param specialties        the model objects
     */
    public SpecialtyResourcePage(PagedResources<SpecialtyResource> specialtyResources,
                                 Page<Specialty> specialties) {
        pageDefaults(specialtyResources, specialties);
    }
}
