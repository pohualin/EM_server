package com.emmisolutions.emmimanager.web.rest.admin.model.patient;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.PatientSearchFilter;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.AdminPatientsResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

import static com.emmisolutions.emmimanager.web.rest.admin.resource.AdminPatientsResource.*;
import static org.springframework.hateoas.TemplateVariable.VariableType.REQUEST_PARAM;
import static org.springframework.hateoas.TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A page of Patient Resource objects
 */
public class AdminPatientResourcePage extends PagedResource<AdminPatientResource> {


    @XmlElement(name = "filter")
    private PatientSearchFilter searchFilter;

    /**
     * Constructor for Patient resource page
     *
     * @param pagedPatientResources the resources
     * @param patientPage           the entity
     */
    public AdminPatientResourcePage(PagedResources<AdminPatientResource> pagedPatientResources, Page<Patient> patientPage, PatientSearchFilter filter) {
        pageDefaults(pagedPatientResources, patientPage);
        addFilterToLinks(filter);
    }

    /**
     * Creates link for GET of reference tags for admin functions
     *
     * @return the link
     */
    public static Link searchLink() {
        Link link = linkTo(methodOn(AdminPatientsResource.class)
                .find(null, null, null, null, null, null)).withRel("patients");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("page", REQUEST_PARAM),
                        new TemplateVariable("size", REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("sort*", REQUEST_PARAM_CONTINUED),
                        new TemplateVariable(NAME_REQUEST_PARAM, REQUEST_PARAM_CONTINUED),
                        new TemplateVariable(ACCESS_CODES_REQUEST_PARAM + "*", REQUEST_PARAM_CONTINUED),
                        new TemplateVariable(PHONES_REQUEST_PARAM + "*", REQUEST_PARAM_CONTINUED),
                        new TemplateVariable(EMAILS_REQUEST_PARAM + "*", REQUEST_PARAM_CONTINUED)
                ));
        return new Link(uriTemplate, link.getRel());
    }

    private void addFilterToLinks(PatientSearchFilter filter) {
        this.searchFilter = filter;
        if (CollectionUtils.isEmpty(links)) {
            return;
        }
        // re-write the links to include the filters
        List<Link> existingLinks = links;
        this.links = new ArrayList<>();
        for (Link link : existingLinks) {
            String rel = link.getRel();
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(link.getHref());
            if (link.isTemplated()) {
                // add args to template
                UriTemplate uriTemplate = new UriTemplate(link.getHref())
                        .with(new TemplateVariables(
                                new TemplateVariable(NAME_REQUEST_PARAM, REQUEST_PARAM_CONTINUED),
                                new TemplateVariable(ACCESS_CODES_REQUEST_PARAM + "*", REQUEST_PARAM_CONTINUED),
                                new TemplateVariable(PHONES_REQUEST_PARAM + "*", REQUEST_PARAM_CONTINUED),
                                new TemplateVariable(EMAILS_REQUEST_PARAM + "*", REQUEST_PARAM_CONTINUED)));
                this.links.add(new Link(uriTemplate.toString(), rel));
            } else {
                // add values
                if (!CollectionUtils.isEmpty(filter.names())) {
                    for (String s : filter.names()) {
                        builder.queryParam(NAME_REQUEST_PARAM, s);
                    }
                }
                if (!CollectionUtils.isEmpty(filter.accessCodes())) {
                    for (String s : filter.accessCodes()) {
                        builder.queryParam(ACCESS_CODES_REQUEST_PARAM, s);
                    }
                }
                if (!CollectionUtils.isEmpty(filter.phones())) {
                    for (String s : filter.phones()) {
                        builder.queryParam(PHONES_REQUEST_PARAM, s);
                    }
                }
                if (!CollectionUtils.isEmpty(filter.emails())) {
                    for (String s : filter.emails()) {
                        builder.queryParam(EMAILS_REQUEST_PARAM, s);
                    }
                }
                this.links.add(new Link(builder.build().encode().toUriString(), rel));
            }
        }
    }
}
