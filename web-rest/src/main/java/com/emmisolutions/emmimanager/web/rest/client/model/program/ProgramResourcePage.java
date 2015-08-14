package com.emmisolutions.emmimanager.web.rest.client.model.program;

import com.emmisolutions.emmimanager.model.program.Program;
import com.emmisolutions.emmimanager.model.program.ProgramSearchFilter;
import com.emmisolutions.emmimanager.model.program.Specialty;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

import static com.emmisolutions.emmimanager.web.rest.client.resource.ProgramsResource.SPECIALTY_ID_REQUEST_PARAM;
import static com.emmisolutions.emmimanager.web.rest.client.resource.ProgramsResource.TERM_REQUEST_PARAM;
import static org.springframework.hateoas.TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED;

/**
 * A page of ProgramResource objects
 */
public class ProgramResourcePage extends PagedResource<ProgramResource> {

    public ProgramResourcePage() {
    }

    /**
     * Make a new resource page
     *
     * @param programResources the paged resources
     * @param programs         the paged entities
     */
    public ProgramResourcePage(ProgramSearchFilter programSearchFilter, PagedResources<ProgramResource> programResources,
                               Page<Program> programs) {
        pageDefaults(programResources, programs);
        addFilterToLinks(programSearchFilter);
    }

    private void addFilterToLinks(ProgramSearchFilter filter) {
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
                                new TemplateVariable(SPECIALTY_ID_REQUEST_PARAM, TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                                new TemplateVariable(TERM_REQUEST_PARAM, REQUEST_PARAM_CONTINUED)
                        ));
                this.links.add(new Link(uriTemplate.toString(), rel));
            } else {
                // add values to existing links
                if (!CollectionUtils.isEmpty(filter.getSpecialties())) {
                    for (Specialty s : filter.getSpecialties()) {
                        builder.queryParam(SPECIALTY_ID_REQUEST_PARAM, s.getId());
                    }
                }
                if (!CollectionUtils.isEmpty(filter.getTerms())) {
                    for (String term : filter.getTerms()) {
                        builder.queryParam(TERM_REQUEST_PARAM, term);
                    }
                }
                this.links.add(new Link(builder.build().encode().toUriString(), rel));
            }
        }
    }
}
