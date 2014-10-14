package com.emmisolutions.emmimanager.web.rest.resource;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.service.ProviderService;
import com.emmisolutions.emmimanager.web.rest.model.groups.ReferenceTagPage;
import com.emmisolutions.emmimanager.web.rest.model.groups.ReferenceTagResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.model.provider.ProviderPage;
import com.emmisolutions.emmimanager.web.rest.model.provider.ProviderResource;
import com.emmisolutions.emmimanager.web.rest.model.provider.ProviderResourceAssembler;

/**
 * Providers REST API
 *
 */

@RestController
@RequestMapping(value = "/webapi", produces = { APPLICATION_JSON_VALUE,
		APPLICATION_XML_VALUE })
public class ProvidersResource {

	@Resource
	ProviderService providerService;

	@Resource
	ProviderResourceAssembler providerResourceAssembler;
	
    @Resource
    ReferenceTagResourceAssembler referenceTagResourceAssembler;

	 /**
     * POST for creating a Provider
     * @param Provider
     * @return ProviderResource
     *
     */
	@RequestMapping(value = "/clients/{clientId}/teams/{teamId}/provider", method = RequestMethod.POST)
	@RolesAllowed({ "PERM_GOD", "PERM_PROVIDER_CREATE" })
	public ResponseEntity<ProviderResource> create(
			@RequestBody Provider provider,
			@PathVariable("teamId")Long teamId,
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
     * GET for searching for providers
     * 
     * @param pageable  paged request
     * @param sort      sorting request
     * @param assembler used to create the PagedResources
     * @param name
     * @param status
     * @return ProviderResource
     */
	@RequestMapping(value = "/clients/{clientId}/teams/{teamId}/provider", method = RequestMethod.GET)
	@RolesAllowed({ "PERM_GOD", "PERM_PROVIDER_LIST" })
	public ResponseEntity<ProviderPage> findAllProvidersByTeam(
			@PageableDefault(size = 10) Pageable pageable,
			@SortDefault(sort = "createdDate") Sort sort,
			@RequestParam(value = "status", required = false) String status,
			PagedResourcesAssembler<Provider> assembler,
			@PathVariable("teamId")Long teamId,
			@PathVariable("clientId") Long clientId) {
		
		Team team = new Team();
		team.setId(teamId);
		Page<Provider> providerPage = providerService.findAllProviders(pageable, team);

		if (providerPage.hasContent()) {
		    return new ResponseEntity<>(new ProviderPage(assembler.toResource(providerPage, providerResourceAssembler), providerPage), HttpStatus.OK);
		} else {
		    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
	@RequestMapping(value = "/provider/{id}", method = RequestMethod.GET)
	@RolesAllowed({ "PERM_GOD", "PERM_PROVIDER_VIEW" })
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
}
