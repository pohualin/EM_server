package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.user.client.reference.UserClientReferenceRole;
import com.emmisolutions.emmimanager.persistence.UserClientReferenceRolePersistence;
import com.emmisolutions.emmimanager.service.UserClientReferenceRoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Reference Group Service Impl
 *
 */
@Service
public class UserClientReferenceRoleServiceImpl implements UserClientReferenceRoleService {

	@Resource
	UserClientReferenceRolePersistence referenceGroupPersistence;

	@Override
	@Transactional(readOnly = true)
	public Page<UserClientReferenceRole> loadUserClientReferenceRoles(Pageable page) {
		return referenceGroupPersistence.loadReferenceRoles(page);
	}
}
