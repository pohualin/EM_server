package com.emmisolutions.emmimanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.UserClientSearchFilter;
import com.emmisolutions.emmimanager.model.user.client.UserClient;

/**
 * The User Client Service
 */
public interface UserClientService {

	/**
	 * Creates a new user client user with the passed UserClient object.
	 *
	 * @param user
	 *            to create
	 * @return the created user
	 */
	UserClient create(UserClient user);

	/**
	 * Update an existing client user with the passed UserClient object.
	 *
	 * @param user
	 *            to update
	 * @return the created user
	 */
	UserClient update(UserClient user);

	/**
	 * Find existing client user with the passed ClientUserSearchFilter.
	 *
	 * @param ClientUserSearchFilter to search
	 * @return pageable UserClient 
	 */
	Page<UserClient> list(Pageable pageable, Long clientId, UserClientSearchFilter filter);

}
