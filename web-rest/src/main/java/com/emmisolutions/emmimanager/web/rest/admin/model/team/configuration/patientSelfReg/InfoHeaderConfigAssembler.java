package com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.patientSelfReg;

import com.emmisolutions.emmimanager.model.InfoHeaderConfig;
import com.emmisolutions.emmimanager.web.rest.admin.resource.InfoHeaderConfigsResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Responsible for creating a GroupResource (which has links) from a Group
 */
@Component
public class InfoHeaderConfigAssembler implements
		ResourceAssembler<InfoHeaderConfig, InfoHeaderConfigResource> {

	@Override
    public InfoHeaderConfigResource toResource(InfoHeaderConfig entity) {
        InfoHeaderConfigResource ret = new InfoHeaderConfigResource();
        ret.setEntity(entity);
        ret.add(linkTo(methodOn(InfoHeaderConfigsResource.class).getById(entity.getId())).withSelfRel());
        return ret;
    }
}
