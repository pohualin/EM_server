package com.emmisolutions.emmimanager.persistence.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.model.TeamProvider_;
import com.emmisolutions.emmimanager.persistence.ProviderPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.persistence.TeamProviderPersistence;
import com.emmisolutions.emmimanager.persistence.repo.TeamProviderRepository;

/**
 * TeamProvider Persistence Implementation
 */
@Repository
public class TeamProviderPersistenceImpl implements TeamProviderPersistence {

    @PersistenceContext
    EntityManager entityManager;
    
    @Resource
    TeamProviderRepository teamProviderRepository;
    
    @Resource
    TeamPersistence teamPersistence;

    @Resource
    ProviderPersistence providerPersistence;

    @Override
    public TeamProvider reload(Long id) {
        return teamProviderRepository.findOne(id);
    }

    @Override
    public TeamProvider save(TeamProvider provider) {
        Team existingTeam = teamPersistence.reload(provider.getTeam());
        Provider existingProvider = providerPersistence.reload(provider.getProvider());
        if (existingTeam == null || existingProvider == null) {
            throw new IllegalArgumentException("Team or Provider is not in the database");
        }
        
        provider.setTeam(existingTeam);
        provider.setProvider(existingProvider);
        return teamProviderRepository.save(provider);
    }

    @Override
    public Page<TeamProvider> findTeamProvidersByTeam(Pageable page, Team team) {
        
    	Pageable pageToFetch;
        if (page == null) {
        	pageToFetch = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        } else {
        	pageToFetch = page;
        }
        Page<TeamProvider> teamProvidersPage = teamProviderRepository.findTeamProvidersByTeam(pageToFetch, team);

        if (teamProvidersPage.hasContent()) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            EntityGraph<TeamProvider> graph = entityManager.createEntityGraph(TeamProvider.class);
            graph.addAttributeNodes(TeamProvider_.teamProviderTeamLocations);
            CriteriaQuery<TeamProvider> cq = cb.createQuery(TeamProvider.class);
            Root<TeamProvider> root = cq.from(TeamProvider.class);
            cq.select(root).where(root.in(teamProvidersPage.getContent()));
            entityManager.createQuery(cq)
                    .setHint(QueryHints.LOADGRAPH, graph)
                    .getResultList();
        }
        return teamProvidersPage;
    }

    @Override
    public TeamProvider findTeamProvidersByProviderAndTeam(Pageable page, Provider provider, Team team){
    	return teamProviderRepository.findTeamProviderByTeamAndProvider(team, provider);
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
    
    @Override
    public List<TeamProvider> getByTeamIdAndProviders(Long teamId, Page<Provider> matchedProviders) {
        if (teamId == null || matchedProviders == null || CollectionUtils.isEmpty(matchedProviders.getContent())) {
            return new ArrayList<>();
        }
        return teamProviderRepository.findByTeamIdAndProviderIn(teamId, matchedProviders.getContent());
    }
}
