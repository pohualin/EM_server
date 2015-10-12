package com.emmisolutions.emmimanager.web.rest.admin.model.program;

import com.emmisolutions.emmimanager.model.program.Program;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/**
 * A program content resource assembler
 */
@Component
public class ProgramContentResourceAssembler implements ResourceAssembler<Program, ProgramContentResource> {

    @Override
    public ProgramContentResource toResource(Program entity) {
        ProgramContentResource ret = new ProgramContentResource();
        ret.setEntity(entity);
        return ret;
    }
}
