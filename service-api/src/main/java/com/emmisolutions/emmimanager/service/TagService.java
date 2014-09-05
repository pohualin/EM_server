package com.emmisolutions.emmimanager.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.Group;
import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.model.TagSearchFilter;

/**
 * Tag Service API
 *
 */
public interface TagService {

	/**
	 * Eager fetch all reference tags
	 * 
	 * @return HashSet of reference tags
	 */
	List<ReferenceTag> fetchReferenceTags();

	/**
	 * Returns a Page of Tags based on the search filter with default Page size
	 */
	Page<Tag> list(TagSearchFilter tagSearchFilter);

	/**
	 * Returns a Page of Tag based on the search filter
	 */
	Page<Tag> list(Pageable pageable, TagSearchFilter tagSearchFilter);

	/**
	 * Find the tag by given id
	 */
	Tag reload(Tag tag);

	/**
	 * Delete the tag by given id
	 */
	List<Tag> saveAll(List<Tag> tags);

	/**
	 * Associate Tag to the Group and save
	 */
	List<Tag> saveAllTagsForGroup(List<Tag> tags, Group group);

}
