package com.emmisolutions.emmimanager.web.rest.admin.resource;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientNote;
import com.emmisolutions.emmimanager.service.ClientNoteService;
import com.emmisolutions.emmimanager.web.rest.admin.model.client.note.ClientNoteResource;

/**
 * ClientNote REST API
 */
@RestController
@RequestMapping(value = "/webapi", produces = { APPLICATION_JSON_VALUE,
        APPLICATION_XML_VALUE })
public class ClientNotesResource {

    @Resource
    ClientNoteService clientNoteService;

    @Resource(name = "clientNoteResourceAssembler")
    ResourceAssembler<ClientNote, ClientNoteResource> clientNoteResourceAssembler;

    /**
     * Get ClientNote by Client
     * 
     * @param clientId
     *            to lookup
     * @return a ClientNote
     */
    @RequestMapping(value = "/clients/{clientId}/client_note", method = RequestMethod.GET)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
    public ResponseEntity<ClientNoteResource> getByClient(
            @PathVariable("clientId") Long clientId) {
        ClientNote clientNote = clientNoteService.findByClient(new Client(
                clientId));
        if (clientNote != null) {
            return new ResponseEntity<>(
                    clientNoteResourceAssembler.toResource(clientNote),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Create a ClientNote
     * 
     * @param clientId
     *            to use
     * @param clientNote
     *            to create
     * @return saved ClientNoteResource
     */
    @RequestMapping(value = "/clients/{clientId}/client_note", method = RequestMethod.POST, consumes = {
            APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE })
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER" })
    public ResponseEntity<ClientNoteResource> create(
            @PathVariable("clientId") Long clientId,
            @RequestBody ClientNote clientNote) {
        clientNote.setClient(new Client(clientId));
        ClientNote saved = clientNoteService.create(clientNote);

        if (saved == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(
                    clientNoteResourceAssembler.toResource(saved),
                    HttpStatus.CREATED);
        }
    }

    /**
     * Update an existing ClientNote
     * 
     * @param clientId
     *            to use
     * @param clientNote
     *            to update
     * @return a ClientNoteResource with updated ClientNote
     */
    @RequestMapping(value = "/clients/{clientId}/client_note", method = RequestMethod.PUT, consumes = {
            APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE })
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER" })
    public ResponseEntity<ClientNoteResource> update(
            @PathVariable("clientId") Long clientId,
            @RequestBody ClientNote clientNote) {
        clientNote.setClient(new Client(clientId));
        ClientNote saved = clientNoteService.update(clientNote);

        if (saved == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(
                    clientNoteResourceAssembler.toResource(saved),
                    HttpStatus.OK);
        }
    }
}
