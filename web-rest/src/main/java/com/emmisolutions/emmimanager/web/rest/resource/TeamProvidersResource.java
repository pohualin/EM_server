package com.emmisolutions.emmimanager.web.rest.resource;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

import java.util.List;

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

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.model.TeamProviderTeamLocationSaveRequest;
import com.emmisolutions.emmimanager.service.TeamProviderService;
import com.emmisolutions.emmimanager.web.rest.model.provider.TeamProviderPage;
import com.emmisolutions.emmimanager.web.rest.model.provider.TeamProviderResource;
import com.emmisolutions.emmimanager.web.rest.model.provider.TeamProviderResourceAssembler;

/**
 * TeamProviders REST API
 *
 */

@RestController
@RequestMapping(value = "/webapi", produces = { APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE })
public class TeamProvidersResource {

	@Resource
	TeamProviderService teamProviderService;

	@Resource
	TeamProviderResourceAssembler teamProviderResourceAssembler;

	/**
	 * GET for a list of all teamProviders for a given team
	 *
	 * @param page	paged request
	 * @param sort  sorting request
	 * @param assembler    used to create the PagedResources
	 * @param name
	 * @param status
	 * @param teamId
	 * @return ProviderResource
	 */
	@RequestMapping(value = "/teams/{teamId}/teamProviders", method = RequestMethod.GET)
	@RolesAllowed({ "PERM_GOD", "PERM_TEAM_PROVIDER_LIST" })
	public ResponseEntity<TeamProviderPage> list(
			@PathVariable("teamId") Long teamId,
			@PageableDefault(size = 10) Pageable page,
			@SortDefault(sort = "id") Sort sort,
			@RequestParam(value = "status", required = false) String status,
			PagedResourcesAssembler<TeamProvider> assembler,
			@RequestParam(value = "name", required = false) String name) {

		Team tofind = new Team();
		tofind.setId(teamId);

		Page<TeamProvider> teamProviderPage = teamProviderService.findTeamProvidersByTeam(page, tofind);
		if (teamProviderPage.hasContent()) {
			return new ResponseEntity<>(new TeamProviderPage(assembler.toResource(teamProviderPage, teamProviderResourceAssembler), teamProviderPage), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}

	/**
	 * POST to associate list of providers to a given team
	 *
	 * @param page	paged request
	 * @param sort  sorting request
	 * @param assembler    used to create the PagedResources
	 * @param name
	 * @param status
	 * @param teamId
	 * @return ProviderResource
	 */
/*	@RequestMapping(value = "/teams/{teamId}/teamProviders", method = RequestMethod.POST)
	@RolesAllowed({ "PERM_GOD", "PERM_TEAM_PROVIDER_CREATE" })
	public ResponseEntity<List<TeamProvider>> associateProvidersToTeam(
			@PathVariable("teamId") Long teamId,
			@RequestBody List<Provider> providers) {

		Team tofind = new Team();
		tofind.setId(teamId);

		List<TeamProvider> teamProviders = teamProviderService.associateProvidersToTeam(providers, tofind);
		if (!teamProviders.isEmpty()) {
			return new ResponseEntity<>(teamProviders, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}*/

	
	@RequestMapping(value = "/teams/{teamId}/teamProviders", method = RequestMethod.POST)
	@RolesAllowed({ "PERM_GOD", "PERM_TEAM_PROVIDER_CREATE" })
	public ResponseEntity<List<TeamProvider>> associateProvidersToTeam(
			@PathVariable("teamId") Long teamId,
			@RequestBody List<TeamProviderTeamLocationSaveRequest> providers) {

		Team tofind = new Team();
		tofind.setId(teamId);

		List<TeamProvider> teamProviders = teamProviderService.associateProvidersToTeam(providers, tofind);
		if (!teamProviders.isEmpty()) {
			return new ResponseEntity<>(teamProviders, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * GET for teamProvider by id
	 *
	 * @param id to load
	 * @return TeamProviderResource or NO_CONTENT on fail
	 */
	@RequestMapping(value = "/teamProviders/{teamProviderId}", method = RequestMethod.GET)
	@RolesAllowed({ "PERM_GOD", "PERM_TEAM_PROVIDER_VIEW" })
	public ResponseEntity<TeamProviderResource> getById(
			@PathVariable("teamProviderId") Long teamProviderId) {
		TeamProvider teamProvider = new TeamProvider();
		teamProvider.setId(teamProviderId);
		teamProvider = teamProviderService.reload(teamProvider);
		if (teamProvider == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(teamProviderResourceAssembler.toResource(teamProvider), HttpStatus.OK);
		}
	}

	/**
	 * DELETE for deletion of a teamProvider from a team
	 *
	 * @return void
	 */
	@RequestMapping(value = "/teamProviders/{teamProviderId}", method = RequestMethod.DELETE)
	@RolesAllowed({ "PERM_GOD", "PERM_TEAM_PROVIDER_REMOVE" })
	public void deleteProviderFromTeamProvider(@PathVariable Long teamProviderId) {
		TeamProvider teamProvider = new TeamProvider();
		teamProvider.setId(teamProviderId);
	    teamProviderService.delete(teamProvider);
	}
}
