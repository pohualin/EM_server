package com.emmisolutions.emmimanager.web.rest.resource;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientSearchFilter;
import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.web.rest.model.client.ClientPage;
import com.emmisolutions.emmimanager.web.rest.model.client.ClientResource;
import com.emmisolutions.emmimanager.web.rest.model.client.ClientResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.model.client.ReferenceData;
import com.emmisolutions.emmimanager.web.rest.model.user.UserPage;
import com.emmisolutions.emmimanager.web.rest.model.user.UserResourceForAssociationsAssembler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

;

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
    ClientResourceAssembler clientResourceAssembler;

    @Resource(name = "userResourceForAssociationsAssembler")
    UserResourceForAssociationsAssembler userResourceAssembler;

    @RequestMapping(value = "/clients/{id}", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_CLIENT_VIEW"})
    public ResponseEntity<ClientResource> get(@PathVariable("id") Long id) {
        Client toFind = new Client();
        toFind.setId(id);
        toFind = clientService.reload(toFind);
        if (toFind != null) {
            return new ResponseEntity<>(clientResourceAssembler.toResource(toFind), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    @RequestMapping(value = "/clients",
            method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_CLIENT_LIST"})
    public ResponseEntity<ClientPage> list(
            @PageableDefault(size = 50) Pageable pageable,
            @SortDefault(sort = "id") Sort sort,
            @RequestParam(value = "name", required = false) Set<String> names,
            @RequestParam(value = "status", required = false) String status,
            PagedResourcesAssembler<Client> assembler) {

        // create the search filter
        ClientSearchFilter clientSearchFilter = new ClientSearchFilter(names, status);

        // find the page of clients
        Page<Client> clientPage = clientService.list(pageable, clientSearchFilter);

        if (clientPage.hasContent()) {
            // create a ClientPage containing the response
            return new ResponseEntity<>(
                    new ClientPage(assembler.toResource(clientPage, clientResourceAssembler), clientPage, clientSearchFilter),
                    HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "/clients/ref", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_CLIENT_CREATE", "PERM_CLIENT_EDIT"})
    public ReferenceData getReferenceData() {
        return new ReferenceData();
    }

    @RequestMapping(value = "/clients/ref/potentialOwners", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_CLIENT_CREATE", "PERM_CLIENT_EDIT"})
    public ResponseEntity<UserPage> getOwnersReferenceData(@PageableDefault(size = 50) Pageable pageable,
                                                           @SortDefault(sort = "id") Sort sort,
                                                           PagedResourcesAssembler<User> assembler) {
        Page<User> userPage = clientService.listPotentialContractOwners(pageable);
        if (userPage.hasContent()) {
            return new ResponseEntity<>(
                    new UserPage(assembler.toResource(userPage, userResourceAssembler)),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }


    @RequestMapping(value = "/clients",
            method = RequestMethod.POST,
            consumes = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE}
    )
    @RolesAllowed({"PERM_GOD", "PERM_CLIENT_CREATE"})
    public ResponseEntity<ClientResource> create(@RequestBody Client client) {
        client = clientService.create(client);
        if (client == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(clientResourceAssembler.toResource(client), HttpStatus.CREATED);
        }
    }

    @RequestMapping(value = "/clients",
            method = RequestMethod.PUT,
            consumes = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE}
    )
    @RolesAllowed({"PERM_GOD", "PERM_CLIENT_EDIT"})
    public ResponseEntity<ClientResource> update(@RequestBody Client client) {
        client = clientService.update(client);
        if (client == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(clientResourceAssembler.toResource(client), HttpStatus.OK);
        }
    }
}
