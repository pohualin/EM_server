package com.emmisolutions.emmimanager.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
	 * Saves a Tag
	 */
	Tag save(Tag tag);

	/**
	 *	Returns a Page of Tags based on the search filter with default Page size
	 */
	Page<Tag> list(TagSearchFilter tagSearchFilter);

	/**
	 * Returns a Page of Tag based on the search filter
	 */
	Page<Tag> list(Pageable pageable, TagSearchFilter tagSearchFilter);

	/**
	 * Creates a tag and associate itself to group
	 */
	Tag create(Tag group, Long groupId);

	/**
	 * Updates the given tag
	 */
	Tag update(Tag tag);

	/**
	 * Find the tag by given id
	 */
	Tag reload(Tag tag);
	
	/**
	 * Delete the tag by given id
	 */
	void remove(Tag tag);
	
	List<Tag> saveAll(List<Tag> tags);
	
	List<Tag> updateAll(List<Tag> tags);

	void removeAll(List<Tag> tags);


}

