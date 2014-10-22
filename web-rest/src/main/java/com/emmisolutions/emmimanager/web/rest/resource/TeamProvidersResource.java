package com.emmisolutions.emmimanager.web.rest.resource;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;

import org.hibernate.annotations.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.service.ProviderService;
import com.emmisolutions.emmimanager.service.TeamProviderService;
import com.emmisolutions.emmimanager.web.rest.model.groups.ReferenceTagResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.model.provider.ProviderResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.model.provider.TeamProviderPage;
import com.emmisolutions.emmimanager.web.rest.model.provider.TeamProviderResource;
import com.emmisolutions.emmimanager.web.rest.model.provider.TeamProviderResourceAssembler;

/**
 * Providers REST API
 *
 */

@RestController
@RequestMapping(value = "/webapi", produces = { APPLICATION_JSON_VALUE,
		APPLICATION_XML_VALUE })
public class TeamProvidersResource {

	@Resource
	ProviderService providerService;

	@Resource
	TeamProviderService teamProviderService;
	
	@Resource
	ProviderResourceAssembler providerResourceAssembler;
	
	@Resource
	TeamProviderResourceAssembler teamProviderResourceAssembler;
    
	@Resource
    ReferenceTagResourceAssembler referenceTagResourceAssembler;

	 /**
     * GET for searching for providers
     * 
     * @param page  paged request
     * @param sort      sorting request
     * @param assembler used to create the PagedResources
     * @param name
     * @param status
     * @return ProviderResource
     */
	@RequestMapping(value = "/teamProviders", method = RequestMethod.GET)
	@RolesAllowed({ "PERM_GOD", "PERM_PROVIDER_LIST" })
	public ResponseEntity<TeamProviderPage> list(
			@PageableDefault(size = 10) Pageable page,
			@SortDefault(sort = "id") Sort sort,
			@RequestParam(value = "status", required = false) String status,
			PagedResourcesAssembler<TeamProvider> assembler,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam Team team) {

		Page<TeamProvider> teamProviderPage = teamProviderService.findTeamProvidersByTeam(page, team);

		if (teamProviderPage.hasContent()) {
		    return new ResponseEntity<>(new TeamProviderPage(assembler.toResource(teamProviderPage, teamProviderResourceAssembler), teamProviderPage), HttpStatus.OK);
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
	@RequestMapping(value = "/teamProviders/{teamProviderId}", method = RequestMethod.GET)
	@RolesAllowed({ "PERM_GOD", "PERM_PROVIDER_VIEW" })
	public ResponseEntity<TeamProviderResource> getById(
			@PathVariable("providerId") Long providerId, 
			@PathVariable("teamId") Long teamId,
			@PathVariable("clientId") Long clientId) {
		TeamProvider teamProvider = new TeamProvider();
		teamProvider.setId(providerId);
		teamProvider = teamProviderService.reload(teamProvider);
		if (teamProvider == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(
					teamProviderResourceAssembler.toResource(teamProvider),
					HttpStatus.OK);
		}
	}
	
	 /**
     * DELETE for deleting for provider from a team
     */
	@RequestMapping(value = "/teamProviders/{teamProviderId}", method = RequestMethod.DELETE)
	@RolesAllowed({ "PERM_GOD", "PERM_PROVIDER_REMOVE" })
	public void deleteProviderFromTeamProvider(
			@PathVariable("providerId")Long providerId,
			@PathVariable("teamId")Long teamId,
			@PathVariable("clientId") Long clientId
			) {
		Provider provider = new Provider();
		provider.setId(providerId);
		Team team = new Team();
		team.setId(teamId);
		try {
			teamProviderService.deleteProviderFromTeamProvider(provider, team);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
