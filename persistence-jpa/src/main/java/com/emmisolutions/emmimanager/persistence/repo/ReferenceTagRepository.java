package com.emmisolutions.emmimanager.persistence.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.emmisolutions.emmimanager.model.ReferenceTag;
/**
 * Spring Data Repo for Reference Tag
 */
public interface ReferenceTagRepository extends
		JpaRepository<ReferenceTag, Long>,
		JpaSpecificationExecutor<ReferenceTag> {
	 /**
     * Find ReferenceGroupType for given name
     * @param Long id of the group
     * @param Pageable pageable
     * @return Page<ReferenceTag>
     */
	Page<ReferenceTag> findAllByGroupIdEquals(Long groupId, Pageable pageable);
}
