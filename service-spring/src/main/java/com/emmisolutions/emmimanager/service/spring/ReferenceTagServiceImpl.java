package com.emmisolutions.emmimanager.service.spring;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.emmisolutions.emmimanager.persistence.repo.ReferenceTagRepository;
import com.emmisolutions.emmimanager.service.ReferenceTagService;

@Service
public class ReferenceTagServiceImpl implements ReferenceTagService {

	@Resource
	ReferenceTagRepository referenceTagRepository;

	public Page<ReferenceTag> findAllByGroupIdEquals(Long groupId, Pageable page) {
		return referenceTagRepository.findAllByGroupIdEquals(groupId, page);
	}

}
