package com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.patientSelfReg;

import com.emmisolutions.emmimanager.model.Strings;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/**
 * Creates StringsResource objects from Strings objects
 */
@Component
public class StringsResourceAssembler implements ResourceAssembler<Strings, StringsResource> {

    @Override
    public StringsResource toResource(Strings entity) {
        StringsResource ret = new StringsResource();
        ret.setEntity(entity);
        return ret;
    }

}
