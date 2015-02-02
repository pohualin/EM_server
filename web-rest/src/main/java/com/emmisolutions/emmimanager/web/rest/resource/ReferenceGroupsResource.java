package com.emmisolutions.emmimanager.web.rest.resource;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.emmisolutions.emmimanager.model.RefGroupSaveRequest;
import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.service.ReferenceGroupService;
import com.emmisolutions.emmimanager.service.ReferenceTagService;
import com.emmisolutions.emmimanager.service.TagService;
import com.emmisolutions.emmimanager.web.rest.model.groups.ReferenceGroupResource;
import com.emmisolutions.emmimanager.web.rest.model.groups.ReferenceGroupResourceAssembler;

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
    ReferenceGroupResourceAssembler referenceGroupResourceAssembler;

    @Resource
    TagService tagService;
    
    @RequestMapping(value="/referenceGroups/{id}", method = RequestMethod.PUT)
    @RolesAllowed({"PERM_GOD"})
    public ResponseEntity<ReferenceGroupResource> updateReferenceGroup(@RequestBody ReferenceGroup refGroup){
        ReferenceGroup group = referenceGroupService.updateReferenceGroup(refGroup);
        if (group == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(referenceGroupResourceAssembler.toResource(group), HttpStatus.OK);
        }
    }
    
    @RequestMapping(value="/referenceGroups/{id}", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD"})
    public ResponseEntity<ReferenceGroupResource> getReferenceGroup(@PathVariable ("id") Long id){
        ReferenceGroup group = referenceGroupService.reload(id);
        if (group == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(referenceGroupResourceAssembler.toResource(group), HttpStatus.OK);
        }
    }
    
    @RequestMapping(value="/referenceGroups", method = RequestMethod.POST)
    @RolesAllowed({"PERM_GOD"})
    public ResponseEntity<ReferenceGroup> createReferenceGroup(@RequestBody RefGroupSaveRequest groupSaveRequest){
        ReferenceGroup refGroup = referenceGroupService.saveReferenceGroupAndReferenceTags(groupSaveRequest);
        if (refGroup == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(refGroup, HttpStatus.OK);
        }
    }
}