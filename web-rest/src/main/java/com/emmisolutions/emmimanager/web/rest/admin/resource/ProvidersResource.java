package com.emmisolutions.emmimanager.web.rest.admin.resource;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.service.ClientProviderService;
import com.emmisolutions.emmimanager.service.ProviderService;
import com.emmisolutions.emmimanager.web.rest.admin.model.groups.ReferenceTagPage;
import com.emmisolutions.emmimanager.web.rest.admin.model.provider.*;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
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

import static com.emmisolutions.emmimanager.model.ProviderSearchFilter.StatusFilter.fromStringOrActive;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * Providers REST API
 */

@RestController
@RequestMapping(value = "/webapi", produces = {APPLICATION_JSON_VALUE,
        APPLICATION_XML_VALUE})
public class ProvidersResource {

    @Resource
    ProviderService providerService;

    @Resource
    ClientProviderService clientProviderService;

    @Resource
    ProviderResourceAssembler providerResourceAssembler;

    @Resource
    ProviderClientResourceAssembler providerClientResourceAssembler;

    @Resource
    ProviderSpecialtyResourceAssembler providerSpecialtyResourceAssembler;

    /**
     * POST for creating a Provider
     *
     * @param provider the provider
     * @param clientId the client id
     * @param teamId   the team id
     * @return ProviderResource
     */
    @RequestMapping(value = "/clients/{clientId}/teams/{teamId}/provider", method = RequestMethod.POST)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<ProviderResource> create(
            @RequestBody Provider provider,
            @PathVariable("teamId") Long teamId,
            @PathVariable("clientId") Long clientId
            ) {

        Team team = new Team();
        team.setId(teamId);
        Provider createdProvider = providerService.create(provider, team);

        if (createdProvider == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(
                    providerResourceAssembler.toResource(createdProvider),
                    HttpStatus.CREATED);
        }

    }

    /**
     * GET to retrieve ReferenceData Specialty types for Provider.
     *
     * @param pageable  paged request
     * @param sort      sorting request
     * @param assembler used to create PagedResources
     * @return ReferenceTagPage matching the search request
     */
    @RequestMapping(value = "/referenceDataSpecialties", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<ProviderSpecialtyPage> getRefData(@PageableDefault(size = 10) Pageable pageable,
                                                       @SortDefault(sort = "id") Sort sort,
                                                       PagedResourcesAssembler<ProviderSpecialty> assembler) {

        Page<ProviderSpecialty> specialtyPage = providerService.findAllSpecialties(pageable);

        if (specialtyPage.hasContent()) {
            return new ResponseEntity<>(new ProviderSpecialtyPage(assembler.toResource(specialtyPage, providerSpecialtyResourceAssembler), specialtyPage), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * GET To get provider by id
     *
     * @param id to load
     * @return ProviderResource or NO_CONTENT on fail
     */
    @RequestMapping(value = "/providers/{id}", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<ProviderResource> getById(@PathVariable("id") Long id) {
        Provider provider = new Provider();
        provider.setId(id);
        provider = providerService.reload(provider);
        if (provider == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(
                    providerResourceAssembler.toResource(provider),
                    HttpStatus.OK);
        }
    }

    /**
     * GET for searching for providers
     *
     * @param page      paged request
     * @param sort      sorting request
     * @param assembler used to create the PagedResources
     * @param name      filter
     * @param status    a filter for providers
     * @return ProviderResource
     */
    @RequestMapping(value = "/providers", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name="size", defaultValue="10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name="page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name="sort", defaultValue="lastName,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<ProviderPage> list(
            @PageableDefault(size = 10, sort="lastName") Pageable page,
            @SortDefault(sort = "lastName") Sort sort,
            @RequestParam(value = "status", required = false) String status,
            PagedResourcesAssembler<Provider> assembler,
            @RequestParam(value = "name", required = false) String name) {

        ProviderSearchFilter filter = new ProviderSearchFilter(fromStringOrActive(status), name);

        Page<Provider> providerPage = providerService.list(page, filter);

        if (providerPage.hasContent()) {
            return new ResponseEntity<>(new ProviderPage(assembler.toResource(providerPage, providerResourceAssembler), providerPage, filter), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * GET for provider reference data
     *
     * @return reference data for providers (status)
     */
    @RequestMapping(value = "/providersReferenceData", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ReferenceData getReferenceData() {
        return new ReferenceData();
    }

    /**
     * GET to find existing clients for a provider.
     *
     * @param providerId  the provider id
     * @param pageable  the page to request
     * @param sort      sorting
     * @param assembler used to create the PagedResources
     * @return Page of ClientProviderResource objects or NO_CONTENT
     */
    @RequestMapping(value = "/providers/{providerId}/clients",
        method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    @ApiOperation("finds existing ClientProviders by providerId")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
        @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
        @ApiImplicitParam(name = "sort", defaultValue = "client.name,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<ProviderClientResourcePage> currentClients(
        @PathVariable Long providerId,
        @PageableDefault(size = 10, sort = "client.name", direction = Sort.Direction.ASC) Pageable pageable,
        Sort sort, PagedResourcesAssembler<ClientProvider> assembler) {
        Page<ClientProvider> clientProviderPage = clientProviderService.findByProvider(new Provider(providerId), pageable);
        if (clientProviderPage.hasContent()) {
            return new ResponseEntity<>(
                new ProviderClientResourcePage(assembler.toResource(clientProviderPage, providerClientResourceAssembler), clientProviderPage, null),
                HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * PUT to update the provider
     *
     * @param provider to update
     * @return ProviderResource or INTERNAL_SERVER_ERROR if update were unsuccessful
     */
    @RequestMapping(value = "/providers",
            method = RequestMethod.PUT,
            consumes = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE}
    )
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<ProviderResource> update(@RequestBody Provider provider) {
        Provider updatedProvider = providerService.update(provider);
        if (updatedProvider == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(providerResourceAssembler.toResource(updatedProvider), HttpStatus.OK);
        }
    }
    
    /**
     * POST to create the provider
     *
     * @param provider to update
     * @return ProviderResource or INTERNAL_SERVER_ERROR if update were unsuccessful
     */
    @RequestMapping(value = "/providers",
            method = RequestMethod.POST,
            consumes = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE}
    )
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<ProviderResource> create(@RequestBody Provider provider) {
        provider = providerService.create(provider);
        if (provider == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(providerResourceAssembler.toResource(provider), HttpStatus.OK);
        }
    }
}
