package com.emmisolutions.emmimanager.web.rest.admin.model.client.note;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.ClientNote;
import com.emmisolutions.emmimanager.web.rest.admin.resource.ClientNotesResource;

/**
 * Creates a ClientNoteResource from a ClientNote
 */
@Component
public class ClientNoteResourceAssembler implements
        ResourceAssembler<ClientNote, ClientNoteResource> {

    /**
     * Compose ClientNoteResource to return
     */
    @Override
    public ClientNoteResource toResource(ClientNote entity) {
        ClientNoteResource ret = new ClientNoteResource();
        if (entity.getId() != null) {
            ret.add(linkTo(
                    methodOn(ClientNotesResource.class).get(entity.getId()))
                    .withSelfRel());
        }
        ret.setEntity(entity);
        return ret;
    }
}
