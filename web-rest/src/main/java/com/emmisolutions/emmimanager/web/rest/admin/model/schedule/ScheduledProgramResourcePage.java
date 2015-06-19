package com.emmisolutions.emmimanager.web.rest.admin.model.schedule;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgramSearchFilter;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.AdminSchedulesResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

import static com.emmisolutions.emmimanager.web.rest.admin.resource.AdminSchedulesResource.*;
import static org.springframework.hateoas.TemplateVariable.VariableType.REQUEST_PARAM;
import static org.springframework.hateoas.TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A page of ScheduledProgramResource objects
 */
public class ScheduledProgramResourcePage extends PagedResource<ScheduledProgramResource> {

    @XmlElement(name = "filter")
    private ScheduledProgramSearchFilter searchFilter;

    /**
     * Constructor with both resource and programs
     *
     * @param scheduledProgramResources the resources created from scheduledPrograms
     * @param scheduledPrograms         the entities
     * @param filter                    the filter used
     */
    public ScheduledProgramResourcePage(PagedResources<ScheduledProgramResource> scheduledProgramResources,
                                        Page<ScheduledProgram> scheduledPrograms,
                                        ScheduledProgramSearchFilter filter) {
        pageDefaults(scheduledProgramResources, scheduledPrograms);
        addFilterToLinks(filter);
    }

    /**
     * Creates link for GET to find AdminSchedulesResource objects
     *
     * @return the link
     */
    public static Link searchLink() {
        Link link = linkTo(methodOn(AdminSchedulesResource.class)
                .find(null, null, null, null, null)).withRel("scheduledPrograms");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("page", REQUEST_PARAM),
                        new TemplateVariable("size", REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("sort*", REQUEST_PARAM_CONTINUED),
                        new TemplateVariable(ACCESS_CODES_REQUEST_PARAM + "*", REQUEST_PARAM_CONTINUED),
                        new TemplateVariable(PATIENT_REQUEST_PARAM + "*", REQUEST_PARAM_CONTINUED),
                        new TemplateVariable(EXPIRED_REQUEST_PARAM, REQUEST_PARAM_CONTINUED)
                ));
        return new Link(uriTemplate, link.getRel());
    }

    private void addFilterToLinks(ScheduledProgramSearchFilter filter) {
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
                                new TemplateVariable(ACCESS_CODES_REQUEST_PARAM + "*", REQUEST_PARAM_CONTINUED),
                                new TemplateVariable(PATIENT_REQUEST_PARAM + "*", REQUEST_PARAM_CONTINUED),
                                new TemplateVariable(EXPIRED_REQUEST_PARAM, REQUEST_PARAM_CONTINUED)

                        ));
                this.links.add(new Link(uriTemplate.toString(), rel));
            } else {
                // add values
                if (!CollectionUtils.isEmpty(filter.accessCodes())) {
                    for (String s : filter.accessCodes()) {
                        builder.queryParam(ACCESS_CODES_REQUEST_PARAM, s);
                    }
                }
                if (!CollectionUtils.isEmpty(filter.patients())) {
                    for (Patient patient : filter.patients()) {
                        builder.queryParam(PATIENT_REQUEST_PARAM, patient.getId());
                    }
                }
                if (filter.includeExpired()) {
                    builder.queryParam(EXPIRED_REQUEST_PARAM, filter.includeExpired());
                }
                this.links.add(new Link(builder.build().encode().toUriString(), rel));
            }
        }
    }
}
