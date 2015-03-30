package com.emmisolutions.emmimanager.web.rest.client.model.program;

import com.emmisolutions.emmimanager.model.program.Program;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/**
 * A program resource assembler
 */
@Component
public class ProgramResourceAssembler implements ResourceAssembler<Program, ProgramResource> {

    @Override
    public ProgramResource toResource(Program entity) {
        ProgramResource ret = new ProgramResource();
        ret.setEntity(entity);
        return ret;
    }
}
