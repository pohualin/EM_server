package com.emmisolutions.emmimanager.web.rest.admin.resource;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.TagService;
import com.emmisolutions.emmimanager.service.TeamService;
import com.emmisolutions.emmimanager.service.TeamTagService;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.TeamTagPage;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.TeamTagResource;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.TeamTagResourceAssembler;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * TeamTags REST API.
 */
@RestController
@RequestMapping(value = "/webapi",
        produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE}
)
public class TeamTagsResource {

    @Resource
    TeamTagService teamTagService;

    @Resource
    TeamTagResourceAssembler teamTagResourceAssembler;

    @Resource
    TeamService teamService;

    @Resource
    TagService tagService;

    @Resource
    ClientService clientService;

    /**
     * GET to search for TeamTags
     *
     * @param pageable  paged request
     * @param assembler used to create the PagedResources
     * @param teamId    current teamId
     * @return ClientPage or NO_CONTENT
     */
    @RequestMapping(value = "/teams/{teamId}/tags", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "id,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<TeamTagPage> list(
            @PathVariable("teamId") Long teamId,
            @PageableDefault(size = 10, sort = "id") Pageable pageable,
            PagedResourcesAssembler<TeamTag> assembler) {

        Team toFind = new Team();
        toFind.setId(teamId);
        toFind = teamService.reload(toFind);
        // find the page of clients
        Page<TeamTag> teamTagPage = teamTagService.findAllTeamTagsWithTeam(pageable, toFind);

        TeamTagSearchFilter teamTagSearchFilter = new TeamTagSearchFilter();

        if (teamTagPage.hasContent()) {
            // create a TeamTagPage containing the response
            PagedResources<TeamTagResource> teamTagResourceSupports = assembler.toResource(teamTagPage, teamTagResourceAssembler);
            TeamTagPage teamTagPage1 = new TeamTagPage(teamTagResourceSupports, teamTagPage, teamTagSearchFilter);
            return new ResponseEntity<>(teamTagPage1, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * POST to create new Team, Tag association
     *
     * @param teamId to associate tags with
     * @param tagSet to associate with team
     * @return TeamTagResource or INTERNAL_SERVER_ERROR if it could not be saved
     */
    @RequestMapping(value = "/teams/{teamId}/tags", method = RequestMethod.POST,
            consumes = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE}
    )
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public java.util.List<TeamTag> create(@PathVariable("teamId") Long teamId, @RequestBody Set<Tag> tagSet) {
        Team toFind = new Team();
        toFind.setId(teamId);

        return teamTagService.save(toFind, tagSet);
    }

    /**
     * DELETE to delete new Team, Tag association
     *
     * @param teamTagId with tag to delete
     * @return NO_CONTENT
     */
    @RequestMapping(value = "teamTags/{teamTagId}", method = RequestMethod.DELETE,
            consumes = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE}
    )
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<TeamTagResource> deleteTeamTag(@PathVariable("teamTagId") Long teamTagId) {
        TeamTag teamTag = new TeamTag();
        teamTag.setId(teamTagId);

        teamTagService.deleteSingleTeamTag(teamTag);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * POST to sa new Team, Tag association
     *
     * @param teamId to associate tags with
     * @param tag    to associate with team
     * @return TeamTagResource or INTERNAL_SERVER_ERROR if it could not be saved
     */
    @RequestMapping(value = "/teams/{teamId}/saveTag", method = RequestMethod.POST,
            consumes = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE}
    )
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<TeamTagResource> saveTeamTag(@PathVariable("teamId") Long teamId, @RequestBody Tag tag) {
        Team toFind = new Team();
        toFind.setId(teamId);
        teamTagService.saveSingleTeamTag(toFind, tag);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * GET to get a teamTag
     *
     * @param teamTagId to get
     * @return TeamTagResource or NO_CONTENT
     */
    @RequestMapping(value = "/teamTags/{teamTagId}", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<TeamTagResource> getTeamTag(@PathVariable("teamTagId") Long teamTagId) {
        TeamTag toFind = new TeamTag();
        toFind.setId(teamTagId);

        toFind = teamTagService.reload(toFind);
        if (toFind != null) {
            return new ResponseEntity<>(teamTagResourceAssembler.toResource(toFind), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * GET teamTags with tag
     *
     * @return List of TeamTag objects or INTERNAL_SERVER_ERROR if the list is empty
     */
    @RequestMapping(value = "/clients/{clientId}/team-tags", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "50", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "id,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<TeamTagPage> teamTagsWithTags(
            @PathVariable("clientId") Long clientId,
            @PageableDefault(size = 50) Pageable pageable,
            @SortDefault(sort = "id") Sort sort,
            PagedResourcesAssembler<TeamTag> assembler,
            @RequestParam(value = "tagIds", required = false) List<Long> tagIds,
            @RequestParam(value = "status", required = false) String status) {
        TeamTagSearchFilter teamTagSearchFilter = new TeamTagSearchFilter();

        Set<Tag> tagSet = new HashSet<>();
        if (tagIds != null) {
            for (Long tagId : tagIds) {
                tagSet.add(new Tag(tagId));
            }
        }

        teamTagSearchFilter.setTagSet(tagSet);
        teamTagSearchFilter.setClient(clientService.reload(new Client(clientId)));
        teamTagSearchFilter.setStatus(TeamTagSearchFilter.StatusFilter.fromStringOrActive(status));

        Page<TeamTag> teamTagPage = teamTagService.findTeamsWithTag(pageable, teamTagSearchFilter);

        if (teamTagPage.hasContent()) {
            PagedResources<TeamTagResource> teamTagResourceSupports = assembler.toResource(teamTagPage, teamTagResourceAssembler);
            TeamTagPage teamTagPage1 = new TeamTagPage(teamTagResourceSupports, teamTagPage, teamTagSearchFilter);
            return new ResponseEntity<>(teamTagPage1, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
