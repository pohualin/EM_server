package com.emmisolutions.emmimanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.ReferenceTag;

public interface ReferenceTagService {

	Page<ReferenceTag> findAllByGroupIdEquals(Long groupId, Pageable page);

}
