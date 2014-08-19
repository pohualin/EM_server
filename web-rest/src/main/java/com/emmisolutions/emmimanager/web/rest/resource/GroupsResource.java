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

import com.emmisolutions.emmimanager.model.Group;
import com.emmisolutions.emmimanager.model.GroupSearchFilter;
import com.emmisolutions.emmimanager.service.GroupService;
import com.emmisolutions.emmimanager.web.rest.model.client.GroupPage;
import com.emmisolutions.emmimanager.web.rest.model.client.GroupResource;
import com.emmisolutions.emmimanager.web.rest.model.client.GroupResourceAssembler;

/**
 * Groups REST API.
 */

@RestController
@RequestMapping(value = "/webapi",
        produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE}
)
public class GroupsResource {

    @Resource
    GroupService groupService;

    @Resource
    GroupResourceAssembler groupResourceAssembler;


    /**
     * GET to search for groups by client id
     *
     * @param pageable  paged request
     * @param sort      sorting request
     * @param status    to filter by
     * @param assembler used to create the PagedResources
     * @param names     to filter by
     * @return GroupPage or NO_CONTENT
     */
    @RequestMapping(value = "/clients/{clientId}/groups",
            method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_CLIENT_LIST"})
    public ResponseEntity<GroupPage> listGroupsByClientID(
            @PageableDefault(size = 50) Pageable pageable,
            @SortDefault(sort = "id") Sort sort,
            PagedResourcesAssembler<Group> assembler,
            @RequestParam(value = "clientId") Long clientId) {

        GroupSearchFilter groupSearchFilter = new GroupSearchFilter(clientId);
        Page<Group> groupPage = groupService.list(pageable, groupSearchFilter);

        if (groupPage.hasContent()) {
            return new ResponseEntity<>(
                    new GroupPage(assembler.toResource(groupPage, groupResourceAssembler), groupPage, groupSearchFilter),
                    HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
    /**
     * POST to create new group
     *
     * @param group 		to create
     * @param clientId 		ID of the client to associate group with
     * @return GroupResource or INTERNAL_SERVER_ERROR if it could not be created
     */
    @RequestMapping(value = "/clients/{clientId}/groups",
            method = RequestMethod.POST,
            consumes = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE}
    )
    @RolesAllowed({"PERM_GOD", "PERM_CLIENT_CREATE"})
    public ResponseEntity<GroupResource> create(@RequestBody Group group, 
    		@RequestParam(value = "clientId") Long clientId) {

    	group = groupService.create(group, clientId);

        if (group == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(groupResourceAssembler.toResource(group), HttpStatus.CREATED);
        }
    } 
    
    /**
     * PUT to update the group
     *
     * @param group to update
     * @return GroupResource or INTERNAL_SERVER_ERROR if update were unsuccessful
     */
    @RequestMapping(value = "/clients/{clientId}/groups",
            method = RequestMethod.PUT,
            consumes = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE}
    )
    @RolesAllowed({"PERM_GOD", "PERM_CLIENT_EDIT"})
    public ResponseEntity<GroupResource> update(@RequestBody Group group) {
        group = groupService.update(group);
        if (group == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(groupResourceAssembler.toResource(group), HttpStatus.OK);
        }
    }
    
    /**
     * GET To get group by id
     * 
     * @param id to load
     * @return GroupResource or INTERNAL_SERVER_ERROR on fail
     *
     */
    @RequestMapping(value = "/groups/{id}",
            method = RequestMethod.GET,
            consumes = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE}
    )
    @RolesAllowed({"PERM_GOD", "PERM_CLIENT_EDIT"})
    public ResponseEntity<GroupResource> getGroupById(@PathVariable("id") Long id) {
        Group group = groupService.reload(id);
        if (group == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(groupResourceAssembler.toResource(group), HttpStatus.OK);
        }
    }
    
}