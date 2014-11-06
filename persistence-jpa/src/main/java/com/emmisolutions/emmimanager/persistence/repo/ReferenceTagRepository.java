package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.ReferenceTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data Repo for Reference Tag
 */
public interface ReferenceTagRepository extends JpaRepository<ReferenceTag, Long>, JpaSpecificationExecutor<ReferenceTag> {

    /**
     * Find a page of ReferenceTags for given group type
     *
     * @param name     of the group type
     * @param pageable the page data
     * @return Page<ReferenceTag>
     */
    Page<ReferenceTag> findAllByGroupTypeName(String name, Pageable pageable);
}
