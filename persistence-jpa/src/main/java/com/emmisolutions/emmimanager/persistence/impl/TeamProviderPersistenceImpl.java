package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.persistence.TeamProviderPersistence;
import com.emmisolutions.emmimanager.persistence.repo.TeamProviderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * TeamProvider Persistence Implementation
 */
@Repository
public class TeamProviderPersistenceImpl implements TeamProviderPersistence {

    @Resource
    TeamProviderRepository teamProviderRepository;

    @Override
    public TeamProvider reload(Long id) {
        return teamProviderRepository.findOne(id);
    }

    @Override
    public TeamProvider save(TeamProvider provider) {
        return teamProviderRepository.save(provider);
    }

    @Override
    public Page<TeamProvider> findTeamProvidersByTeam(Pageable page, Team team) {
        return teamProviderRepository.findTeamProvidersByTeam(page, team);
    }

    @Override
    public void delete(TeamProvider provider) {
        teamProviderRepository.delete(provider);
    }

    @Override
    public List<TeamProvider> saveAll(List<TeamProvider> teamProviders) {
        return teamProviderRepository.save(teamProviders);
    }

    @Override
    public long delete(Client client, Provider provider) {
        return teamProviderRepository.deleteByTeamClientAndProvider(client, provider);
    }

    @Override
    public Page<Team> findTeamsBy(Client client, Provider provider, Pageable page) {
        return teamProviderRepository.findTeamsByClientAndProvider(client, provider, page);
    }
}
