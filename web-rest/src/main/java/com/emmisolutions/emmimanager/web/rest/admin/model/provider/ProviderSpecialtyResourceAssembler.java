package com.emmisolutions.emmimanager.web.rest.admin.model.provider;

import com.emmisolutions.emmimanager.model.ProviderSpecialty;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/**
 * Responsible for creating a ProviderSpecialtyResource
 */
@Component
public class ProviderSpecialtyResourceAssembler implements ResourceAssembler<ProviderSpecialty, ProviderSpecialtyResource> {
    @Override
    public ProviderSpecialtyResource toResource(ProviderSpecialty providerSpecialty) {
        return  new ProviderSpecialtyResource(providerSpecialty);
    }
}
