package com.emmisolutions.emmimanager.web.rest.resource;

import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamTag;
import com.emmisolutions.emmimanager.model.TeamTagSearchFilter;
import com.emmisolutions.emmimanager.service.TeamService;
import com.emmisolutions.emmimanager.service.TeamTagService;
import com.emmisolutions.emmimanager.web.rest.model.team.TeamTagPage;
import com.emmisolutions.emmimanager.web.rest.model.team.TeamTagResource;
import com.emmisolutions.emmimanager.web.rest.model.team.TeamTagResourceAssembler;
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

    /**
     * GET to search for TeamTags
     *
     * @param pageable  paged request
     * @param sort      sorting request
     * @param status    to filter by
     * @param assembler used to create the PagedResources
     * @param names     to filter by
     * @param teamId    current teamId
     * @return ClientPage or NO_CONTENT
     */
    @RequestMapping(value = "/teams/{teamId}/tags", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_TEAM_TAG_VIEW"})
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "id,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<TeamTagPage> list(
            @PathVariable("teamId") Long teamId,
            @PageableDefault(size = 100) Pageable pageable,
            @SortDefault(sort = "id") Sort sort,
            @RequestParam(value = "status", required = false) String status,
            PagedResourcesAssembler<TeamTag> assembler,
            @RequestParam(value = "name", required = false) String names) {

        Team toFind = new Team();
        toFind.setId(teamId);
        toFind = teamService.reload(toFind);
        TeamTagSearchFilter teamTagSearchFilter = new TeamTagSearchFilter(teamId);
        // find the page of clients
        Page<TeamTag> teamTagPage = teamTagService.findAllTeamTagsWithTeam(pageable, toFind);

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
    @RolesAllowed({"PERM_GOD", "PERM_TEAM_TAG_CREATE"})
    public void create(@PathVariable("teamId") Long teamId, @RequestBody Set<Tag> tagSet) {
        Team toFind = new Team();
        toFind.setId(teamId);

        teamTagService.save(toFind, tagSet);
    }

//    /**
//     * DELETE to delete new Team, Tag association
//     *
//     * @param teamId with tag to delete
//     * @param tag    to delete
//     */
//    @RequestMapping(value = "/teams/{teamId}/deleteTag", method = RequestMethod.DELETE,
//            consumes = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE}
//    )
//    @RolesAllowed({"PERM_GOD", "PERM_TEAM_TAG_CREATE"})
//    public ResponseEntity<TeamTagResource> deleteTeamTag(@PathVariable("teamId") Long teamId, @RequestBody Tag tag) {
//        Team toFind = new Team();
//        toFind.setId(teamId);
//        teamTagService.deleteSingleTeamTag(toFind, tag);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//
//    /**
//     * POST to sa new Team, Tag association
//     *
//     * @param teamId to associate tags with
//     * @param tag    to associate with team
//     * @return TeamTagResource or INTERNAL_SERVER_ERROR if it could not be saved
//     */
//    @RequestMapping(value = "/teams/{teamId}/saveTag", method = RequestMethod.POST,
//            consumes = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE}
//    )
//    @RolesAllowed({"PERM_GOD", "PERM_TEAM_TAG_CREATE"})
//    public ResponseEntity<TeamTagResource> saveTeamTag(@PathVariable("teamId") Long teamId, @RequestBody Tag tag) {
//        Team toFind = new Team();
//        toFind.setId(teamId);
//        teamTagService.saveSingleTeamTag(toFind, tag);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

    /**
     * GET to get a teamTag
     *
     * @param teamId    to get
     * @param teamTagId to get
     * @return TeamTagResource or NO_CONTENT
     */
    @RequestMapping(value = "/teams/{teamId}/tags/{teamTagId}", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_TEAM_TAG_VIEW"})
    public ResponseEntity<TeamTagResource> getTeamTag(@PathVariable("teamId") Long teamId, @PathVariable("teamTagId") Long teamTagId) {
        TeamTag toFind = new TeamTag();
        toFind.setId(teamId);

        toFind = teamTagService.reload(toFind);
        if (toFind != null) {
            return new ResponseEntity<>(teamTagResourceAssembler.toResource(toFind), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
