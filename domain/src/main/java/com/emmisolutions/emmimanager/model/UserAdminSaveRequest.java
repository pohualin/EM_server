package com.emmisolutions.emmimanager.model;

import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminRole;

import java.util.HashSet;
import java.util.Set;

/**
 * A request for User Admin save with the roles
 *
 */
public class UserAdminSaveRequest {

    private Set<UserAdminRole> roles = new HashSet<>();

	private UserAdmin userAdmin;
	
	@Override
	public String toString() {
		return "UserAdminSaveRequest [roles=" + roles
				+ ", userAdmin=" + userAdmin + "]";
	}

	public Set<UserAdminRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<UserAdminRole> roles) {
		this.roles = roles;
	}

	public UserAdmin getUserAdmin() {
		return userAdmin;
	}

	public void setUserAdmin(UserAdmin userAdmin) {
		this.userAdmin = userAdmin;
	}

}
