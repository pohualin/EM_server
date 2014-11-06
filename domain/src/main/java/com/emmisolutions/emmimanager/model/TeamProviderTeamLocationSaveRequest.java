package com.emmisolutions.emmimanager.model;

import java.util.List;

/**
 * A request for Provider and teamLocations to save a TeamProvider and TeamProviderTeamLocation
 *
 */
public class TeamProviderTeamLocationSaveRequest {

	private Provider provider;

	private List<TeamLocation> teamLocations;

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public List<TeamLocation> getTeamLocations() {
		return teamLocations;
	}

	public void setTeamLocations(List<TeamLocation> teamLocations) {
		this.teamLocations = teamLocations;
	}

	@Override
	public String toString() {
		return "TeamProviderTeamLocationSaveRequest [provider=" + provider
				+ ", teamLocations=" + teamLocations + "]";
	}

}
