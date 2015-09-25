package com.emmisolutions.emmimanager.web.rest.admin.model.program.specialty;

import com.emmisolutions.emmimanager.model.program.Specialty;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/**
 * Creates SpecialtyContentResource objects from Specialty objects
 */
@Component
public class SpecialtyContentResourceAssembler implements ResourceAssembler<Specialty, SpecialtyContentResource> {

    @Override
    public SpecialtyContentResource toResource(Specialty entity) {
        SpecialtyContentResource ret = new SpecialtyContentResource();
        ret.setEntity(entity);
        return ret;
    }

}
