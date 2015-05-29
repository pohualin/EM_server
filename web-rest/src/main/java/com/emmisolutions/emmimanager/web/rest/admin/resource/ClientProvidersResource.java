package com.emmisolutions.emmimanager.web.rest.admin.resource;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.service.ClientProviderService;
import com.emmisolutions.emmimanager.service.ProviderService;
import com.emmisolutions.emmimanager.service.TeamProviderService;
import com.emmisolutions.emmimanager.web.rest.admin.model.clientprovider.ClientProviderFinderResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.admin.model.clientprovider.ClientProviderResource;
import com.emmisolutions.emmimanager.web.rest.admin.model.clientprovider.ClientProviderResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.admin.model.clientprovider.ClientProviderResourcePage;
import com.emmisolutions.emmimanager.web.rest.admin.model.provider.ProviderResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.TeamPage;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.TeamResourceAssembler;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import java.util.HashSet;
import java.util.Set;

import static com.emmisolutions.emmimanager.model.ProviderSearchFilter.StatusFilter.fromStringOrActive;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * ClientProviders REST API.
 */
@RestController
@RequestMapping(value = "/webapi",
    produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE}
)
public class ClientProvidersResource {

    @Resource
    ClientProviderService clientProviderService;

    @Resource
    ProviderService providerService;

    @Resource
    ProviderResourceAssembler providerResourceAssembler;

    @Resource
    ClientProviderResourceAssembler clientProviderResourceAssembler;

    @Resource
    ClientProviderFinderResourceAssembler clientProviderFinderResourceAssembler;

    @Resource
    TeamProviderService teamProviderService;

    @Resource
    TeamResourceAssembler teamResourceAssembler;

    /**
     * GET to find existing client providers for a client.
     *
     * @param clientId  the client
     * @param pageable  the page to request
     * @param assembler used to create the PagedResources
     * @return Page of ClientProviderResource objects or NO_CONTENT
     */
    @RequestMapping(value = "/clients/{clientId}/providers",
        method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    @ApiOperation("finds existing ClientProviders")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
        @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "provider.normalizedName,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<ClientProviderResourcePage> current(
        @PathVariable Long clientId,
        @PageableDefault(size = 10, sort = {"provider.normalizedName"}, direction = Sort.Direction.ASC) Pageable pageable,
        PagedResourcesAssembler<ClientProvider> assembler) {
        Page<ClientProvider> clientProviderPage = clientProviderService.findByClient(new Client(clientId), pageable);
        if (clientProviderPage.hasContent()) {
            return new ResponseEntity<>(
                new ClientProviderResourcePage(assembler.toResource(clientProviderPage, clientProviderResourceAssembler), clientProviderPage, null),
                HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * POST to create a new provider and associated it to a client.
     *
     * @param clientId to use
     * @param provider to create new
     * @return the ClientProviderResource association, success or INTERNAL_SERVER_ERROR if it could not be created
     */
    @RequestMapping(value = "/clients/{clientId}/providers",
        method = RequestMethod.POST,
        consumes = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE}
    )
    @ApiOperation(value = "creates a brand new provider and associates it to an existing client")
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<ClientProviderResource> create(@PathVariable Long clientId, @RequestBody ClientProvider provider) {
        provider.setClient(new Client(clientId));
        ClientProvider clientProvider = clientProviderService.create(provider);
        if (clientProvider == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(clientProviderResourceAssembler.toResource(clientProvider), HttpStatus.CREATED);
        }
    }

    /**
     * GET to find all possible providers that can be used on a client. The object will come back with a link
     * if it is currently associated to the passed client. If it is not currently in use at the passed client,
     * the link will be null.
     *
     * @param clientId  the client
     * @param pageable  the page to request
     * @param assembler used to create the PagedResources
     * @param name      filter
     * @param status    filter
     * @return Page of ClientProviderResource objects or NO_CONTENT
     */
    @RequestMapping(value = "/clients/{clientId}/providers/associate",
        method = RequestMethod.GET)
    @ApiOperation(value = "finds all possible providers that can be associated to a client", notes = "The object will come back with a link, if it is currently associated to the passed client. If it is not currently in use at the passed client, the link will be null.")
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
        @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "normalizedName,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<ClientProviderResourcePage> possible(
        @PathVariable Long clientId,
        @PageableDefault(size = 10, sort = {"normalizedName"}, direction = Sort.Direction.ASC) Pageable pageable,
        PagedResourcesAssembler<ClientProvider> assembler,
        @RequestParam(value = "status", required = false) String status,
        @RequestParam(value = "name", required = false) String name) {

        ProviderSearchFilter filter = new ProviderSearchFilter(fromStringOrActive(status), name);

        Page<ClientProvider> clientProviderPage = clientProviderService.findPossibleProvidersToAdd(
            new Client(clientId), filter, pageable);

        if (clientProviderPage.hasContent()) {
            return new ResponseEntity<>(
                new ClientProviderResourcePage(assembler.toResource(clientProviderPage, clientProviderFinderResourceAssembler), clientProviderPage, filter),
                HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * POST to create new ClientProvider on a Client with a Set of existing Provider objects.
     *
     * @param clientId  on which to create a providers
     * @param providers to attach to the client
     * @return Set of ClientProviderResource objects representing the assocation
     * or INTERNAL_SERVER_ERROR if it could not be created
     */
    @RequestMapping(value = "/clients/{clientId}/providers/associate",
        method = RequestMethod.POST,
        consumes = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE}
    )
    @ApiOperation("create a new ClientProvider on a Client for each existing Provider in a Set")
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<Set<ClientProviderResource>> associate(@PathVariable Long clientId, @RequestBody Set<Provider> providers) {
        Set<ClientProvider> clientProviders = clientProviderService.create(new Client(clientId), providers);
        if (clientProviders == null || clientProviders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            // convert to resources
            Set<ClientProviderResource> ret = new HashSet<>();
            for (ClientProvider clientProvider : clientProviders) {
                ret.add(clientProviderResourceAssembler.toResource(clientProvider));
            }
            return new ResponseEntity<>(ret, HttpStatus.CREATED);
        }
    }
    
    /**
     * GET to find all possible providers not using client for a client.
     * 
     * @param clientId the client
     * @param pageable the page to request
     * @param assembler used to create the PagedResources
     * @param status the status to filter
     * @param name the name to filter
     * @return Page of ClientProviderResource objects or NO_CONTENT
     */
    @RequestMapping(value = "/clients/{clientId}/providers/not_using_client/associate", method = RequestMethod.GET)
    @ApiOperation(value = "finds all possible providers not using client that can be associated to a client")
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "normalizedName,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")})
    public ResponseEntity<ClientProviderResourcePage> possibleProvidersNotUsingClient(
            @PathVariable Long clientId,
            @PageableDefault(size = 10, sort = "normalizedName", direction = Sort.Direction.ASC) Pageable pageable,
            PagedResourcesAssembler<ClientProvider> assembler,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "name", required = false) String name) {

        ProviderSearchFilter filter = new ProviderSearchFilter(
                fromStringOrActive(status), name);
        filter.setNotUsingThisClient(new Client(clientId));

        Page<ClientProvider> clientProviderPage = clientProviderService
                .findPossibleProvidersToAdd(new Client(clientId), filter,
                        pageable);

        if (clientProviderPage.hasContent()) {
            return new ResponseEntity<>(new ClientProviderResourcePage(
                    assembler.toResource(clientProviderPage,
                            clientProviderFinderResourceAssembler),
                    clientProviderPage, filter), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * GET a single client provider
     *
     * @param clientProviderId the actual client provider to load
     * @return ClientProviderResource or NO_CONTENT
     */
    @RequestMapping(value = "/client_providers/{clientProviderId}", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    @ApiOperation("view a ClientProvider by id")
    public ResponseEntity<ClientProviderResource> view(@PathVariable Long clientProviderId) {
        ClientProvider clientProvider = clientProviderService.reload(new ClientProvider(clientProviderId));
        if (clientProvider != null) {
            return new ResponseEntity<>(clientProviderResourceAssembler.toResource(clientProvider), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * DELETE a single client provider
     *
     * @param clientProviderId the association to remove
     */
    @RequestMapping(value = "/client_providers/{clientProviderId}", method = RequestMethod.DELETE)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    @ApiOperation("delete a ClientProvider by id")
    public void delete(@PathVariable Long clientProviderId) {
        clientProviderService.remove(new ClientProvider(clientProviderId));
    }

    /**
     * GET a page of Teams associated to the client provider
     *
     * @param clientProviderId the actual client provider to load
     * @param assembler        to make resources
     * @param pageable         the specification
     * @return page of Teams or NO_CONTENT
     */
    @RequestMapping(value = "/client_providers/{clientProviderId}/teams", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    @ApiOperation("view Teams using ClientProvider")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
        @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
        @ApiImplicitParam(name = "sort", defaultValue = "team.name,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<TeamPage> teams(@PathVariable Long clientProviderId,
                                          PagedResourcesAssembler<Team> assembler,
                                          @PageableDefault(size = 10, sort = "team.name", direction = Sort.Direction.ASC) Pageable pageable) {
        ClientProvider clientProvider = clientProviderService.reload(new ClientProvider(clientProviderId));
        if (clientProvider != null) {
            Page<Team> teamPage = teamProviderService.findTeamsBy(clientProvider.getClient(), clientProvider.getProvider(), pageable);
            if (teamPage.hasContent()) {
                return new ResponseEntity<>(
                    new TeamPage(assembler.toResource(teamPage, teamResourceAssembler), teamPage, null), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * PUT to update a client provider.
     *
     * @param clientProviderId the provider id
     * @param clientProvider   to update
     * @return ProviderResource or INTERNAL_SERVER_ERROR if it could not be created
     */
    @RequestMapping(value = "/client_providers/{clientProviderId}",
        method = RequestMethod.PUT,
        consumes = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE}
    )
    @ApiOperation(value = "update a Provider using the client", notes = "This method updates the Provider and the ClientProvider")
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<ClientProviderResource> update(@PathVariable Long clientProviderId,
                                                         @RequestBody ClientProvider clientProvider) {
        clientProvider = clientProviderService.update(clientProvider);
        if (clientProvider == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(clientProviderResourceAssembler.toResource(clientProvider), HttpStatus.OK);
        }
    }

    /**
     * GET to find a ClientProvider by client id and provider id
     *
     * @param clientId   the client id
     * @param providerId the provider id
     * @return the found ClientProviderResource
     */
    @RequestMapping(value = "/clients/{clientId}/providers/{providerId}", method = RequestMethod.GET)
    @ApiOperation(value = "find ClientProvider by client id and provider id")
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<ClientProviderResource> findByClientIdProviderId(@PathVariable Long clientId, @PathVariable Long providerId) {
        ClientProvider clientProvider = clientProviderService.findByClientIdProviderId(clientId, providerId);
        if (clientProvider == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(clientProviderResourceAssembler.toResource(clientProvider), HttpStatus.OK);
        }
    }
}
