package com.emmisolutions.emmimanager.web.rest.admin.model.case_management;

import com.emmisolutions.emmimanager.model.salesforce.CaseForm;
import com.emmisolutions.emmimanager.web.rest.admin.resource.CasesResource;
import org.springframework.hateoas.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

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
        ret.add(new Link(
                new UriTemplate(linkTo(methodOn(CasesResource.class).query(null, null, new ArrayList<String>()))
                        .withSelfRel().getHref())
                        .with(new TemplateVariables(
                                new TemplateVariable("q",
                                        TemplateVariable.VariableType.REQUEST_PARAM),
                                new TemplateVariable(
                                        "size",
                                        TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                                new TemplateVariable(
                                        "type*",
                                        TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED))), "lookup"));
        return ret;
    }

}
