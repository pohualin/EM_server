package com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.patient_self_reg;

import com.emmisolutions.emmimanager.model.PatientIdLabelConfig;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/**
 * Responsible for creating a GroupResource (which has links) from a Group
 */
@Component
public class PatientIdLabelConfigAssembler implements
		ResourceAssembler<PatientIdLabelConfig, PatientIdLabelConfigResource> {

	@Override
    public PatientIdLabelConfigResource toResource(PatientIdLabelConfig entity) {
        PatientIdLabelConfigResource ret = new PatientIdLabelConfigResource();
//    	ret.add(linkTo(methodOn(GroupsResource.class).getGroupById(entity.getId())).withSelfRel());
//        ret.add(TagPage.createFullSearchLink(entity));
        ret.setEntity(entity);
        return ret;
    }
}
