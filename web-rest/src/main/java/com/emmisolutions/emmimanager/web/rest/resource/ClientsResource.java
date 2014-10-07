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
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
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

import static com.emmisolutions.emmimanager.model.ClientSearchFilter.StatusFilter.fromStringOrActive;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * Clients REST API.
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

    /**
     * GET a single client
     *
     * @param id to load
     * @return ClientResource or NO_CONTENT
     */
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

    /**
     * GET to search for clients
     *
     * @param pageable  paged request
     * @param sort      sorting request
     * @param status    to filter by
     * @param assembler used to create the PagedResources
     * @param names     to filter by
     * @return ClientPage or NO_CONTENT
     */
    @RequestMapping(value = "/clients",
            method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_CLIENT_LIST"})
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name="size", defaultValue="10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name="page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name="sort", defaultValue="name,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<ClientPage> list(
            @PageableDefault(size = 10, sort = "name") Pageable pageable,
            @SortDefault(sort = "name") Sort sort,
            PagedResourcesAssembler<Client> assembler,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "name", required = false) String name) {

        // create the search filter
        ClientSearchFilter clientSearchFilter = new ClientSearchFilter(fromStringOrActive(status), name);

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

    /**
     * GET to Retrieve reference data about clients.
     *
     * @return ReferenceData
     */
    @RequestMapping(value = "/clients/ref", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_CLIENT_CREATE", "PERM_CLIENT_EDIT"})
    public ReferenceData getReferenceData() {
        return new ReferenceData(clientService.getAllClientTypes());
    }

    /**
     * GET to retrieve Potential Owner reference data. This data is paginated
     *
     * @param pageable  paged request
     * @param sort      sorting request
     * @param assembler used to create PagedResources
     * @return UserPage matching the search request
     */
    @RequestMapping(value = "/clients/ref/potentialOwners", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_CLIENT_CREATE", "PERM_CLIENT_EDIT"})
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name="size",  value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name="page",  value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name="sort",  value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<UserPage> getOwnersReferenceData(@PageableDefault(size = 100, sort="firstName") Pageable pageable,
                                                           @SortDefault(sort = "id") Sort sort,
                                                           PagedResourcesAssembler<User> assembler) {
        Page<User> userPage = clientService.listPotentialContractOwners(pageable);
        if (userPage.hasContent()) {
            return new ResponseEntity<>(
                    new UserPage(assembler.toResource(userPage, userResourceAssembler), userPage),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * POST to create new client
     *
     * @param client to create
     * @return ClientResource or INTERNAL_SERVER_ERROR if it could not be created
     */
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

    /**
     * PUT to update the client
     *
     * @param client to update
     * @return ClientResource or INTERNAL_SERVER_ERROR if update were unsuccessful
     */
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
    
   @RequestMapping(value = "/clients/ref/findByNormalizedName", 
            method = RequestMethod.GET)
   @RolesAllowed({"PERM_GOD", "PERM_CLIENT_CREATE", "PERM_CLIENT_EDIT"})
   public ResponseEntity<ClientResource> findByNormalizedName(@RequestParam(value = "normalizedName", required = false)String normalizedName) {

       Client toFind = clientService.findByNormalizedName(normalizedName);
       if (toFind != null) {
           return new ResponseEntity<>(clientResourceAssembler.toResource(toFind), HttpStatus.OK);
       } else {
           return new ResponseEntity<>(HttpStatus.NO_CONTENT);
       } 
    }
}
