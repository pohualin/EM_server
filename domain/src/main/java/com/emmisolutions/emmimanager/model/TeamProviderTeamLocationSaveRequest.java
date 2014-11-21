package com.emmisolutions.emmimanager.model;

import java.util.Set;

/**
 * A request for Provider and teamLocations to save a TeamProvider and
 * TeamProviderTeamLocation
 *
 */
public class TeamProviderTeamLocationSaveRequest {

	private Provider provider;

	private TeamProvider teamProvider;

	private ClientProvider clientProvider;

	private Set<TeamLocation> teamLocations;

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public ClientProvider getClientProvider() {
		return clientProvider;
	}

	public void setClientProvider(ClientProvider clientProvider) {
		this.clientProvider = clientProvider;
	}

	public Set<TeamLocation> getTeamLocations() {
		return teamLocations;
	}

	public void setTeamLocations(Set<TeamLocation> teamLocations) {
		this.teamLocations = teamLocations;
	}

	public TeamProvider getTeamProvider() {
		return teamProvider;
	}

	public void setTeamProvider(TeamProvider teamProvider) {
		this.teamProvider = teamProvider;
	}

	@Override
	public String toString() {
		return "TeamProviderTeamLocationSaveRequest [provider=" + provider
				+ ", teamLocations=" + teamLocations + "]";
	}

}
