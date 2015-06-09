package com.emmisolutions.emmimanager.web.rest.admin.model.patient;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.PatientSearchFilter;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/**
 * A page of Patient Resource objects
 */
public class AdminPatientResourcePage extends PagedResource<AdminPatientResource> {


    @XmlElement(name = "filter")
    private PatientSearchFilter searchFilter;

    public AdminPatientResourcePage() {
    }

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
                                new TemplateVariable("name", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
                this.links.add(new Link(uriTemplate.toString(), rel));
            } else {
                // add values
                if (!CollectionUtils.isEmpty(filter.names())) {
                    for (String s : filter.names()) {
                        builder.queryParam("name", s);
                    }
                }
                this.links.add(new Link(builder.build().encode().toUriString(), rel));
            }
        }
    }
}
