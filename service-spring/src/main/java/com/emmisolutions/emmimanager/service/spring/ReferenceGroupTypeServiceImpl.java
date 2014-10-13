package com.emmisolutions.emmimanager.service.spring;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emmisolutions.emmimanager.model.ReferenceGroupType;
import com.emmisolutions.emmimanager.persistence.ReferenceGroupTypePersistence;
import com.emmisolutions.emmimanager.service.ReferenceGroupTypeService;

/**
 * Reference Group Type Service Impl
 *
 */
@Service
public class ReferenceGroupTypeServiceImpl implements ReferenceGroupTypeService {

	@Resource
	ReferenceGroupTypePersistence referenceGroupTypePersistence;

	@Override
	@Transactional(readOnly = true)
	public ReferenceGroupType findByName(String name) {
		return referenceGroupTypePersistence.findByName(name);
	}

}
