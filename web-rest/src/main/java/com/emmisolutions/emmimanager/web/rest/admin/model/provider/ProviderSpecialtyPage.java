package com.emmisolutions.emmimanager.web.rest.admin.model.provider;

import com.emmisolutions.emmimanager.model.ProviderSpecialty;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A HATEOAS wrapper for a page of ProviderSpecialtyResource objects.
 */
@XmlRootElement(name="specialty-page")
public class ProviderSpecialtyPage extends PagedResource <ProviderSpecialtyResource> {

    /**
     * creates a wrapper for provider specialty resource
     *
     * @param resources
     * @param specialtyPage
     */
    public ProviderSpecialtyPage(PagedResources<ProviderSpecialtyResource> resources, Page<ProviderSpecialty> specialtyPage) {
        pageDefaults(resources, specialtyPage);
    }
}
