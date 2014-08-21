package com.emmisolutions.emmimanager.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.model.TagSearchFilter;

public interface TagPersistence {

	/**
	 * Returns a list of Tags associated to the groupID provided in searchFilter
	 * @param Pageable
	 * @param TagSearchFilter
	 * @return Page of Tags
	 */
	Page<Tag> listTagsByGroupId(Pageable page, TagSearchFilter searchFilter);

	/**
	 * Saves a Tag
	 * @param Tag 	to save
	 * @return saved Tag
	 */
	Tag save(Tag tag);
	
	/**
	 * Reloads a Tag by given ID
	 * @param Long id	of the Tag
	 * @return Tag
	 */
	Tag reload(Tag tag);
	
	/**
	 * @param Long 	id of the Tag to be deleted
	 * @return void
	 */
	void remove(Tag tag);
	
	List<ReferenceTag> fetchAllReferenceTags();
	
	List<Tag> updateAll(List<Tag> tags);
	
	List<Tag> createAll(List<Tag> tags);

	void removeAll(List<Tag> tags);

}
