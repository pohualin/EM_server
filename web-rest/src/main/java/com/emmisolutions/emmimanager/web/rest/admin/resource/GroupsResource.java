package com.emmisolutions.emmimanager.web.rest.admin.resource;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.service.GroupService;
import com.emmisolutions.emmimanager.service.ReferenceGroupService;
import com.emmisolutions.emmimanager.service.TagService;
import com.emmisolutions.emmimanager.web.rest.admin.model.groups.*;
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
import java.util.List;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * Groups REST API.
 */

@RestController
@RequestMapping(value = "/webapi", produces = {APPLICATION_JSON_VALUE,
        APPLICATION_XML_VALUE})
public class GroupsResource {

    @Resource
    GroupService groupService;

    @Resource
    ReferenceGroupService referenceGroupService;

    @Resource
    GroupResourceAssembler groupResourceAssembler;

    @Resource
    ReferenceGroupResourceAssembler referenceGroupResourceAssembler;

    @Resource
    TagService tagService;

    /**
     * GET to search for groups by client id
     *
     * @param pageable  paged request
     * @param sort      sorting request
     * @param assembler used to create the PagedResources
     * @param clientId  to filter by
     * @return GroupPage or NO_CONTENT
     */
    @RequestMapping(value = "/clients/{clientId}/groups", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_USER"})
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "50", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "id,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<GroupPage> listGroupsByClientID(
            @PageableDefault(size = 50) Pageable pageable,
            @SortDefault(sort = "id") Sort sort,
            PagedResourcesAssembler<Group> assembler,
            @PathVariable("clientId") Long clientId) {

        GroupSearchFilter groupSearchFilter = new GroupSearchFilter(clientId);
        Page<Group> groupPage = groupService.list(pageable, groupSearchFilter);

        if (groupPage.hasContent()) {
            return new ResponseEntity<>(new GroupPage(assembler.toResource(groupPage, groupResourceAssembler), groupPage, groupSearchFilter), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }


    /**
     * POST to save the groups and tags
     *
     * @param groupSaveRequests groups and tags to create
     * @param clientId          the client id
     * @return List of Group objects or INTERNAL_SERVER_ERROR if update were unsuccessful
     */
    @RequestMapping(value = "/clients/{clientId}/groups", method = RequestMethod.POST)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_USER"})
    public ResponseEntity<Set<Group>> create(@RequestBody List<GroupSaveRequest> groupSaveRequests,
                                              @PathVariable("clientId") Long clientId) {
        Set<Group> groups = groupService.saveGroupsAndTags(groupSaveRequests, clientId);
        if (groups == null || groups.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(groups, HttpStatus.OK);
        }
    }

/**
     * get to get conflicting teams
     *
     * @param groupSaveRequests groups and tags to create
     * @param clientId          the client id
     * @return List of Group objects or INTERNAL_SERVER_ERROR if update were unsuccessful
     */
    @RequestMapping(value = "/clients/{clientId}/invalidTeam", method = RequestMethod.POST)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_USER"})
    public ResponseEntity<Set<TeamTag>> invalidTeams(@RequestBody List<GroupSaveRequest> groupSaveRequests,
                                                         @PathVariable("clientId") Long clientId) {
        Set<TeamTag> teamTags = groupService.findTeamsPreventingSaveOf(groupSaveRequests, clientId);
        if (teamTags == null || teamTags.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(teamTags, HttpStatus.OK);
        }
    }


    /**
     * GET To get group by id
     *
     * @param id to load
     * @return GroupResource or NO_CONTENT on fail
     */
    @RequestMapping(value = "/groups/{id}", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_USER"})
    public ResponseEntity<GroupResource> getGroupById(@PathVariable("id") Long id) {
        Group group = groupService.reload(id);
        if (group == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(groupResourceAssembler.toResource(group), HttpStatus.OK);
        }
    }

    /**
     * GET to retrieve ReferenceGroup data.
     *
     * @param pageable  paged request
     * @param sort      sorting request
     * @param assembler used to create PagedResources
     * @return groupPage matching the search request
     */
    @RequestMapping(value = "/referenceGroups", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_USER"})
    public ResponseEntity<ReferenceGroupPage> getRefGroups(@PageableDefault(size = 50) Pageable pageable,
                                                           @SortDefault(sort = "id") Sort sort,
                                                           PagedResourcesAssembler<ReferenceGroup> assembler) {

        Page<ReferenceGroup> groupPage = referenceGroupService.loadReferenceGroups(pageable);

        if (groupPage.hasContent()) {
            return new ResponseEntity<>(new ReferenceGroupPage(assembler.toResource(groupPage, referenceGroupResourceAssembler), groupPage), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

}