package com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.patientSelfReg;

import com.emmisolutions.emmimanager.model.Language;
import com.emmisolutions.emmimanager.model.program.Specialty;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/**
 * Creates LanguageResource objects from Language objects
 */
@Component
public class LanguageResourceAssembler implements ResourceAssembler<Language, LanguageResource> {

    @Override
    public LanguageResource toResource(Language entity) {
        LanguageResource ret = new LanguageResource();
        ret.setEntity(entity);
        return ret;
    }

}
