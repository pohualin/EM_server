package com.emmisolutions.emmimanager.web.rest.admin.resource;

import com.emmisolutions.emmimanager.model.RefGroupSaveRequest;
import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.emmisolutions.emmimanager.service.ReferenceGroupService;
import com.emmisolutions.emmimanager.service.ReferenceTagService;
import com.emmisolutions.emmimanager.service.TagService;
import com.emmisolutions.emmimanager.web.rest.admin.model.groups.*;
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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * Reference Groups REST API.
 */

@RestController
@RequestMapping(value = "/webapi", produces = {APPLICATION_JSON_VALUE,
        APPLICATION_XML_VALUE})
public class ReferenceGroupsResource {

    @Resource
    ReferenceGroupService referenceGroupService;

    @Resource
    ReferenceTagService referenceTagService;

    @Resource
    ReferenceGroupResourceAssembler referenceGroupResourceAssembler;

    @Resource
    ReferenceTagResourceAssembler referenceTagResourceAssembler;

    @Resource
    TagService tagService;

    /**
     * Update to a reference group
     *
     * @param id       to update
     * @param refGroup the update
     * @return
     */
    @RequestMapping(value = "/referenceGroups/{id}", method = RequestMethod.PUT)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER"})
    public ResponseEntity<ReferenceGroupResource> updateReferenceGroup(@PathVariable Long id,
                                                                       @RequestBody ReferenceGroup refGroup) {
        refGroup.setId(id);
        ReferenceGroup group = referenceGroupService.updateReferenceGroup(refGroup);
        if (group == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(referenceGroupResourceAssembler.toResource(group), HttpStatus.OK);
        }
    }

    /**
     * Load a particular reference group
     *
     * @param id to load
     * @return the loaded group
     */
    @RequestMapping(value = "/referenceGroups/{id}", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<ReferenceGroupResource> getReferenceGroup(@PathVariable("id") Long id) {
        ReferenceGroup group = referenceGroupService.reload(id);
        if (group == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(referenceGroupResourceAssembler.toResource(group), HttpStatus.OK);
        }
    }

    /**
     * Delete a reference group
     *
     * @param id to delete
     */
    @RequestMapping(value = "/referenceGroups/{id}", method = RequestMethod.DELETE)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public void removeReferenceGroup(@PathVariable("id") Long id) {
        referenceGroupService.delete(new ReferenceGroup(id));
    }

    /**
     * Create new reference groups
     *
     * @param groupSaveRequest a new group to save
     * @return the saved resource
     */
    @RequestMapping(value = "/referenceGroups", method = RequestMethod.POST)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER"})
    public ResponseEntity<ReferenceGroupResource> createReferenceGroup(@RequestBody RefGroupSaveRequest groupSaveRequest) {
        ReferenceGroup refGroup = referenceGroupService.saveReferenceGroupAndReferenceTags(groupSaveRequest);
        if (refGroup == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(referenceGroupResourceAssembler.toResource(refGroup), HttpStatus.OK);
        }
    }

    /**
     * Load all reference groups
     *
     * @param pageable  the page spec
     * @param sort      the sort spec
     * @param assembler to convert ReferenceGroup objects within the page
     * @return the ReferenceGroupPage
     */
    @RequestMapping(value = "/referenceGroups", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<ReferenceGroupPage> getAllReferenceGroups(@PageableDefault(size = 50) Pageable pageable,
                                                                    @SortDefault(sort = "id") Sort sort,
                                                                    PagedResourcesAssembler<ReferenceGroup> assembler) {

        Page<ReferenceGroup> groupPage = referenceGroupService.loadReferenceGroups(pageable);

        if (groupPage.hasContent()) {
            return new ResponseEntity<>(new ReferenceGroupPage(assembler.toResource(groupPage, referenceGroupResourceAssembler), groupPage), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
    /**
     * Load active reference groups
     * 
     * @param the
     *            page spec
     * @param sort
     *            the sort spec
     * @param assembler
     *            to convert ReferenceGroup objects within the page
     * @return the ReferenceGroupPage
     */
    @RequestMapping(value = "/activeReferenceGroups", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<ReferenceGroupPage> getActiveReferenceGroups(@PageableDefault(size = 50) Pageable pageable,
                                                                    @SortDefault(sort = "id") Sort sort,
                                                                    PagedResourcesAssembler<ReferenceGroup> assembler) {

        Page<ReferenceGroup> groupPage = referenceGroupService.loadActiveReferenceGroups(pageable);

        if (groupPage.hasContent()) {
            return new ResponseEntity<>(new ReferenceGroupPage(assembler.toResource(groupPage, referenceGroupResourceAssembler), groupPage), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Get all tags for a group
     *
     * @param pageable   the page spec
     * @param sort       the sort spec
     * @param assembler  to assemble the page of ReferenceTag objects
     * @param refGroupId the reference group id
     * @return a ReferenceTagPage
     */
    @RequestMapping(value = "/referenceGroups/{refGroupId}/referenceTags}", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<ReferenceTagPage> getAllReferenceTagsByGroup(@PageableDefault(size = 50) Pageable pageable,
                                                                       @SortDefault(sort = "id") Sort sort,
                                                                       PagedResourcesAssembler<ReferenceTag> assembler,
                                                                       @PathVariable("refGroupId") Long refGroupId) {

        Page<ReferenceTag> tagPage = referenceTagService.findAllTagsByGroup(new ReferenceGroup(refGroupId), pageable);

        if (tagPage.hasContent()) {
            return new ResponseEntity<>(new ReferenceTagPage(assembler.toResource(tagPage, referenceTagResourceAssembler), tagPage), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * GET To see if a group is allowed to be deleted
     *
     * @param id of the group
     * @return NOT_ACCEPTABLE if not allowed to delete, OK if allowed
     */
    @RequestMapping(value = "/referenceGroups/{id}/deletable", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<Void> deletable(@PathVariable("id") Long id) {
        if (!referenceGroupService.isDeletable(new ReferenceGroup(id))) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}