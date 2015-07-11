package com.emmisolutions.emmimanager.web.rest.admin.model.case_management;

import com.emmisolutions.emmimanager.model.salesforce.CaseType;
import com.emmisolutions.emmimanager.web.rest.admin.resource.CasesResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Creates CaseTypeResource from CaseType objects
 */
@Component
public class CaseTypeResourceAssembler implements ResourceAssembler<CaseType, CaseTypeResource> {

    @Override
    public CaseTypeResource toResource(CaseType entity) {
        CaseTypeResource ret = new CaseTypeResource();
        ret.setEntity(entity);
        ret.add(linkTo(methodOn(CasesResource.class).create(entity.getId())).withRel("blankForm"));
        return ret;
    }

}
