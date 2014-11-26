package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.user.client.reference.UserClientReferenceRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Reference Group Service
 *
 */
public interface UserClientReferenceRoleService {
	/**
	 * loads reference groups
	 *
	 * @param page specification
	 * @return Page<UserClientReferenceRole>
	 */
	Page<UserClientReferenceRole> loadUserClientReferenceRoles(Pageable page);
}
