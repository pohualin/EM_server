package com.emmisolutions.emmimanager.model;

import java.util.HashSet;
import java.util.Set;

/**
 * A request for Location and teamProviders to save a TeamLocation and TeamProviderTeamLocation
 *
 */
public class TeamLocationTeamProviderSaveRequest {

	private Set<TeamProvider> providers = new HashSet<TeamProvider>();

	private Location location;
	
	public Set<TeamProvider> getProviders() {
		return providers;
	}

	public void setProviders(Set<TeamProvider> providers) {
		this.providers = providers;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "TeamLocationTeamProviderSaveRequest [providers=" + providers
				+ ", location=" + location + "]";
	}

}
