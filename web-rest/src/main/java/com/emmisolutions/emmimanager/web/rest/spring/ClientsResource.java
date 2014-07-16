package com.emmisolutions.emmimanager.web.rest.spring;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.web.rest.model.ClientPageResource;
import com.emmisolutions.emmimanager.web.rest.model.ClientResource;
import com.emmisolutions.emmimanager.web.rest.spring.util.EndpointHelper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * Clients
 */
@RestController
@RequestMapping(value = "/webapi",
        produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE}
)
public class ClientsResource {

    @Resource
    ClientService clientService;

    @Resource
    EndpointHelper endpointHelper;

    @RequestMapping(value = "/clients/{id}", method = RequestMethod.GET)
    public ResponseEntity<ClientResource> get(@PathVariable("id") Long id) {
        Client toFind = new Client();
        toFind.setId(id);
        toFind = clientService.reload(toFind);
        if (toFind != null) {
            return new ResponseEntity<>(new ClientResource(toFind), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    @RequestMapping(value = "/clients", method = RequestMethod.GET)
    public ResponseEntity<ClientPageResource> list(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "max", required = false, defaultValue = "10") Integer max,
            @RequestParam(value = "sort", required = false, defaultValue = "id:asc;") String sort,
            @RequestParam(value = "name", required = false, defaultValue = "") String nameFilter,
            @RequestParam(value = "status", required = false, defaultValue = "") String statusFilter) {
        Page<Client> clients = clientService.list(
                endpointHelper.createPageable(page, max, sort));
        if (clients.hasContent()) {
            return new ResponseEntity<>(new ClientPageResource(clients), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "/clients",
            method = RequestMethod.POST,
            consumes = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<ClientResource> create(Client client) {
        client = clientService.create(client);
        if (client == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(new ClientResource(client), HttpStatus.CREATED);
        }
    }
}
