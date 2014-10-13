package com.emmisolutions.emmimanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.ReferenceTag;
/**
 * Reference Tag Service
 * 
 */
public interface ReferenceTagService {
	/**
	 * Finds all reference tags for given reference group id
	 * @param Long groupId
	 * @param Pageable page
	 * @return Page<ReferenceTag>
	 */
	Page<ReferenceTag> findAllByGroupIdEquals(Long groupId, Pageable page);

}
