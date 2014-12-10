package com.emmisolutions.emmimanager.web.rest.resource;

import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.model.TagSearchFilter;
import com.emmisolutions.emmimanager.service.TagService;
import com.emmisolutions.emmimanager.web.rest.model.client.TagPage;
import com.emmisolutions.emmimanager.web.rest.model.client.TagResource;
import com.emmisolutions.emmimanager.web.rest.model.client.TagResourceAssembler;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * Tags REST API.
 */
@RestController
@RequestMapping(value = "/webapi", produces = {APPLICATION_JSON_VALUE,
    APPLICATION_XML_VALUE})
public class TagsResource {

    @Resource
    TagService tagService;

    @Resource
    TagResourceAssembler tagResourceAssembler;


    /**
     * GET to search for tags by group id
     *
     * @param pageable  paged request
     * @param sort      sorting request
     * @param status    to filter by
     * @param assembler used to create the PagedResources
     * @param groupId   the group id
     * @return TagPage or NO_CONTENT
     */
    @RequestMapping(value = "/groups/{groupId}/tags", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_TAG_LIST"})
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "size", defaultValue = "50", value = "number of items on a page", dataType = "integer", paramType = "query"),
        @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
        @ApiImplicitParam(name = "sort", defaultValue = "id,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<TagPage> listTagsByGroupID(
        @PageableDefault(size = 50) Pageable pageable,
        @SortDefault(sort = "id") Sort sort,
        PagedResourcesAssembler<Tag> assembler,
        @PathVariable("groupId") Long groupId) {

        TagSearchFilter tagSearchFilter = new TagSearchFilter(groupId);
        Page<Tag> tagPage = tagService.list(pageable, tagSearchFilter);

        if (tagPage.hasContent()) {
            return new ResponseEntity<>(new TagPage(assembler.toResource(tagPage, tagResourceAssembler), tagPage, tagSearchFilter), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }


    /**
     * GET To get tag by id
     *
     * @param id to load
     * @return TagResource or NO_CONTENT on fail
     */
    @RequestMapping(value = "/tags/{id}", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_TAG_VIEW"})
    public ResponseEntity<TagResource> getTagById(@PathVariable("id") Long id) {
        Tag tag = new Tag();
        tag.setId(id);
        tag = tagService.reload(tag);
        if (tag == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(tagResourceAssembler.toResource(tag), HttpStatus.OK);
        }
    }
}
