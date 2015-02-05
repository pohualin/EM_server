package com.emmisolutions.emmimanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.ReferenceGroupType;

/**
 * Reference Group Type Service
 *
 */
public interface ReferenceGroupTypeService {

    /**
     * finds a page of reference group types
     * @param page
     * @return
     */
    Page<ReferenceGroupType> findAll(Pageable page);

}
