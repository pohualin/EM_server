package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.user.client.reference.UserClientReferenceRole;
import com.emmisolutions.emmimanager.model.user.client.reference.UserClientReferenceRoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Reference Role Persistence
 */
public interface UserClientReferenceRolePersistence {

	/**
	 * loads reference groups
	 *
	 * @param page specification
	 * @return a Page of ReferenceRole objects
	 */
	Page<UserClientReferenceRole> loadReferenceRoles(Pageable page);


    /**
     * Loads a UserClientReferenceRoleType by id
     * @param type to reload
     * @return the loaded type
     */
    UserClientReferenceRoleType reload(UserClientReferenceRoleType type);
}
