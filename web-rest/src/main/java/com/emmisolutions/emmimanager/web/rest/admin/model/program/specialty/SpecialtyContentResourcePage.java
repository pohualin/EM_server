package com.emmisolutions.emmimanager.web.rest.admin.model.program.specialty;

import com.emmisolutions.emmimanager.model.program.Specialty;
import com.emmisolutions.emmimanager.web.rest.client.model.PagedResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;

/**
 * A page of specialty content resources
 */
public class SpecialtyContentResourcePage extends PagedResource<SpecialtyContentResource> {

    /**
     * Creates a page of SpecialtyContentResource objects for serialization to
     * the front
     *
     * @param specialtyResources the paged resources
     * @param specialties        the model objects
     */
    public SpecialtyContentResourcePage(PagedResources<SpecialtyContentResource> specialtyResources,
                                 Page<Specialty> specialties) {
        pageDefaults(specialtyResources, specialties);
    }
}
