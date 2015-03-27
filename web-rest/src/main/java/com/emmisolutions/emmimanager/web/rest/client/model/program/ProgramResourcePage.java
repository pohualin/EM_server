package com.emmisolutions.emmimanager.web.rest.client.model.program;

import com.emmisolutions.emmimanager.model.program.Program;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;

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
    public ProgramResourcePage(PagedResources<ProgramResource> programResources,
                               Page<Program> programs) {
        pageDefaults(programResources, programs);
    }
}
