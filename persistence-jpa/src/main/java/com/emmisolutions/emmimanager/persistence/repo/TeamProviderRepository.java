package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * TeamProvider repository
 */
public interface TeamProviderRepository extends JpaRepository<TeamProvider, Long>, JpaSpecificationExecutor<TeamProvider> {

    /**
     * Finds all team-providers for a given team
     *
     * @param page page specification
     * @param team team to use to locate providers
     * @return page of team-providers
     */
    Page<TeamProvider> findTeamProvidersByTeam(Pageable page, Team team);


    /**
     * Delete all TeamProvider objects for a Client and Provider
     *
     * @param client   to point to
     * @param provider to use
     * @return number deleted
     */
    long deleteByTeamClientAndProvider(Client client, Provider provider);

    /**
     * Finds a page of TeamProviders for a client and provider
     *
     * @param client   the client
     * @param provider the provider
     * @param page     specification
     * @return page of Teams
     */
    @Query("select tp.team from TeamProvider tp where tp.team.client = :client and tp.provider = :provider")
    Page<Team> findTeamsByClientAndProvider(@Param("client") Client client, @Param("provider") Provider provider, Pageable page);

}
