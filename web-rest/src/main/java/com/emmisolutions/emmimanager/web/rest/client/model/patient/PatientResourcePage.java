package com.emmisolutions.emmimanager.web.rest.client.model.patient;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.PatientSearchFilter;
import com.emmisolutions.emmimanager.model.ProviderSearchFilter;
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
public class PatientResourcePage extends PagedResource<PatientResource> {


    @XmlElement(name = "filter")
    private PatientSearchFilter searchFilter;

    public PatientResourcePage() {
    }

    /**
     * constructor for Patient resource page
     * @param pagedPatientResources
     * @param patientPage
     */
    public PatientResourcePage(PagedResources<PatientResource> pagedPatientResources, Page<Patient> patientPage, PatientSearchFilter filter) {
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
                if (!CollectionUtils.isEmpty(filter.getNames())) {
                    for (String s : filter.getNames()) {
                        builder.queryParam("name", s);
                    }
                }
                this.links.add(new Link(builder.build().encode().toUriString(), rel));
            }
        }
    }
}
