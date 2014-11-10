package com.emmisolutions.emmimanager.model;

import java.util.Set;

/**
 * A request for Provider and teamLocations to save a TeamProvider and TeamProviderTeamLocation
 *
 */
public class TeamProviderTeamLocationSaveRequest {

	private Provider provider;

	private Set<TeamLocation> teamLocations;

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public Set<TeamLocation> getTeamLocations() {
		return teamLocations;
	}

	public void setTeamLocations(Set<TeamLocation> teamLocations) {
		this.teamLocations = teamLocations;
	}

	@Override
	public String toString() {
		return "TeamProviderTeamLocationSaveRequest [provider=" + provider
				+ ", teamLocations=" + teamLocations + "]";
	}

}
