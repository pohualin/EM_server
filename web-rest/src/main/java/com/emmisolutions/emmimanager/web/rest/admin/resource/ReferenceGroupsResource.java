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
    
    @RequestMapping(value="/referenceGroups/{id}", method = RequestMethod.PUT)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER"})
    public ResponseEntity<ReferenceGroupResource> updateReferenceGroup(@PathVariable Long id,
                                                                       @RequestBody ReferenceGroup refGroup){
        ReferenceGroup group = referenceGroupService.updateReferenceGroup(refGroup);
        if (group == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(referenceGroupResourceAssembler.toResource(group), HttpStatus.OK);
        }
    }
    
    @RequestMapping(value="/referenceGroups/{id}", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<ReferenceGroupResource> getReferenceGroup(@PathVariable ("id") Long id){
        ReferenceGroup group = referenceGroupService.reload(id);
        if (group == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(referenceGroupResourceAssembler.toResource(group), HttpStatus.OK);
        }
    }
    
    @RequestMapping(value="/referenceGroups", method = RequestMethod.POST)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER"})
    public ResponseEntity<ReferenceGroupResource> createReferenceGroup(@RequestBody RefGroupSaveRequest groupSaveRequest){
        ReferenceGroup refGroup = referenceGroupService.saveReferenceGroupAndReferenceTags(groupSaveRequest);
        if (refGroup == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(referenceGroupResourceAssembler.toResource(refGroup), HttpStatus.OK);
        }
    }

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
}