package com.emmisolutions.emmimanager.web.rest.resource;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

import java.util.Arrays;
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
import org.springframework.web.bind.annotation.RestController;

import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.model.TagSearchFilter;
import com.emmisolutions.emmimanager.service.TagService;
import com.emmisolutions.emmimanager.web.rest.model.client.TagPage;
import com.emmisolutions.emmimanager.web.rest.model.client.TagResource;
import com.emmisolutions.emmimanager.web.rest.model.client.TagResourceAssembler;

/**
 * Tags REST API.
 */
@RestController
@RequestMapping(value = "/webapi", produces = { APPLICATION_JSON_VALUE,
		APPLICATION_XML_VALUE })
public class TagsResource {

	@Resource
	TagService tagService;

	@Resource
	TagResourceAssembler tagResourceAssembler;

	/**
	 * POST to create new tag
	 *
	 * @param tag array		to create
	 * @return List<Tag> or INTERNAL_SERVER_ERROR if it could not be created
	 */
	@RequestMapping(value = "/clients/tags", method = RequestMethod.POST, consumes = {
			APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE })
	@RolesAllowed({ "PERM_GOD", "PERM_CLIENT_CREATE" })
	public ResponseEntity<List<Tag>> create(@RequestBody Tag[] tags) {

		List<Tag> tagsList = tagService.saveAll(Arrays.asList(tags));
		
		if (tagsList == null || tagsList.isEmpty()) {
			return new ResponseEntity<List<Tag>>(HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<>(tagsList, HttpStatus.OK);
		}	}
	
	/**
	 * PUT to update tags
	 *
	 * @param tag array to update
	 * @return List<Tag> or INTERNAL_SERVER_ERROR if it could not be updated
	 */
	@RequestMapping(value = "/clients/tags", method = RequestMethod.PUT, consumes = {
			APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE })
	@RolesAllowed({ "PERM_GOD", "PERM_CLIENT_CREATE" })
	public ResponseEntity<List<Tag>> update(@RequestBody Tag[] tags) {

		List<Tag> tagsList = tagService.updateAll(Arrays.asList(tags));

		if (tagsList == null || tagsList.isEmpty()) {
			return new ResponseEntity<List<Tag>>(HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<>(tagsList, HttpStatus.OK);
		}
	}
	
	/**
	 * DELETE to delete tags
	 *
	 * @param Tag[] tags to update
	 * @return List<Tag> or INTERNAL_SERVER_ERROR if it could not be updated
	 */
	@RequestMapping(value = "/clients/tags", method = RequestMethod.DELETE, consumes = {
			APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE })
	@RolesAllowed({ "PERM_GOD", "PERM_CLIENT_CREATE" })
	public void remove(@RequestBody Tag[] tags) throws InternalError {
		try {
			tagService.removeAll(Arrays.asList(tags));
		} catch (Exception e) {
			throw new InternalError("Delete of tags failed");

		}
	}
	

	/**
	 * GET to search for tags by group id
	 *
	 * @param pageable paged request
	 * @param sort sorting request
	 * @param status to filter by
	 * @param assembler used to create the PagedResources
	 * @return TagPage or NO_CONTENT
	 */
	@RequestMapping(value = "/groups/{groupId}/tags", method = RequestMethod.GET)
	@RolesAllowed({ "PERM_GOD", "PERM_CLIENT_LIST" })
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
	 * @param id 	to load
	 * @return TagResource or NO_CONTENT on fail
	 *
	 */
	@RequestMapping(value = "/clients/tags/{id}", method = RequestMethod.GET)
	@RolesAllowed({ "PERM_GOD", "PERM_CLIENT_EDIT" })
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