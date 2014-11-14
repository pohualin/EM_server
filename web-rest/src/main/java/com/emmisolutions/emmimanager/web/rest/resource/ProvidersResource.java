package com.emmisolutions.emmimanager.web.rest.resource;

import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.ProviderSearchFilter;
import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.service.ProviderService;
import com.emmisolutions.emmimanager.web.rest.model.groups.ReferenceTagPage;
import com.emmisolutions.emmimanager.web.rest.model.groups.ReferenceTagResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.model.provider.ProviderPage;
import com.emmisolutions.emmimanager.web.rest.model.provider.ProviderResource;
import com.emmisolutions.emmimanager.web.rest.model.provider.ProviderResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.model.provider.ReferenceData;
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
    ProviderResourceAssembler providerResourceAssembler;

    @Resource
    ReferenceTagResourceAssembler referenceTagResourceAssembler;

    /**
     * POST for creating a Provider
     *
     * @param provider the provider
     * @param clientId the client id
     * @param teamId   the team id
     * @return ProviderResource
     */
    @RequestMapping(value = "/clients/{clientId}/teams/{teamId}/provider", method = RequestMethod.POST)
    @RolesAllowed({"PERM_GOD", "PERM_PROVIDER_CREATE"})
    public ResponseEntity<ProviderResource> create(
            @RequestBody Provider provider,
            @PathVariable("teamId") Long teamId,
            @PathVariable("clientId") Long clientId) {

        Team team = new Team();
        team.setId(teamId);
        provider = providerService.create(provider, team);

        if (provider == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(
                    providerResourceAssembler.toResource(provider),
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
    @RolesAllowed({"PERM_GOD", "PERM_PROVIDER_VIEW"})
    public ResponseEntity<ReferenceTagPage> getRefData(@PageableDefault(size = 10) Pageable pageable,
                                                       @SortDefault(sort = "id") Sort sort,
                                                       PagedResourcesAssembler<ReferenceTag> assembler) {

        Page<ReferenceTag> tagPage = providerService.findAllSpecialties(pageable);

        if (tagPage.hasContent()) {
            return new ResponseEntity<>(new ReferenceTagPage(assembler.toResource(tagPage, referenceTagResourceAssembler), tagPage), HttpStatus.OK);
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
    @RolesAllowed({"PERM_GOD", "PERM_PROVIDER_VIEW"})
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
    @RolesAllowed({"PERM_GOD", "PERM_PROVIDER_LIST"})
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
    @RolesAllowed({"PERM_GOD", "PERM_PROVIDER_VIEW"})
    public ReferenceData getReferenceData() {
        return new ReferenceData();
    }
}
