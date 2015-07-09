package com.emmisolutions.emmimanager.web.rest.admin.model.case_management;

import com.emmisolutions.emmimanager.model.salesforce.CaseForm;
import com.emmisolutions.emmimanager.web.rest.admin.resource.CasesResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Creates a CaseFormResource from a CaseForm
 */
@Component
public class CaseFormResourceAssembler implements ResourceAssembler<CaseForm, CaseFormResource> {

    @Override
    public CaseFormResource toResource(CaseForm entity) {
        CaseFormResource ret = new CaseFormResource();
        ret.setEntity(entity);
        ret.add(linkTo(methodOn(CasesResource.class).save(entity)).withRel("save"));
        return ret;
    }

}
