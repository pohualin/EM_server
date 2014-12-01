package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.user.client.team.reference.UserClientReferenceTeamRole;
import com.emmisolutions.emmimanager.model.user.client.team.reference.UserClientReferenceTeamRoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Reference team level role persistence
 */
public interface UserClientReferenceTeamRolePersistence {

	/**
	 * loads reference groups
	 *
	 * @param page specification
	 * @return a Page of ReferenceTeamRole objects
	 */
	Page<UserClientReferenceTeamRole> loadReferenceTeamRoles(Pageable page);


    /**
     * Loads a UserClientReferenceTeamRoleType by id
     * @param type to reload
     * @return the loaded type
     */
    UserClientReferenceTeamRoleType reload(UserClientReferenceTeamRoleType type);
}
