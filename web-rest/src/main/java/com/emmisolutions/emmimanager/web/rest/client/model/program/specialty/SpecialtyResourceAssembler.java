package com.emmisolutions.emmimanager.web.rest.client.model.program.specialty;

import com.emmisolutions.emmimanager.model.program.Specialty;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/**
 * Creates SpecialtyResource objects from Specialty objects
 */
@Component
public class SpecialtyResourceAssembler implements ResourceAssembler<Specialty, SpecialtyResource> {

    @Override
    public SpecialtyResource toResource(Specialty entity) {
        SpecialtyResource ret = new SpecialtyResource();
        ret.setEntity(entity);
        return ret;
    }

}
