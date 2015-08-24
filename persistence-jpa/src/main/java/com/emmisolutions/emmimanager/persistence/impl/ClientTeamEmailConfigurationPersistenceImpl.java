package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.ClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.persistence.ClientTeamEmailConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ClientTeamEmailConfigurationRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Persistence implementation for ClientTeamEmailConfiguration entity.
 */
@Repository
public class ClientTeamEmailConfigurationPersistenceImpl implements ClientTeamEmailConfigurationPersistence {

    @Resource
    ClientTeamEmailConfigurationRepository clientTeamEmailConfigurationRepository;

    @Override
    public ClientTeamEmailConfiguration find(Long teamId) {
        return clientTeamEmailConfigurationRepository.findByTeamId(teamId);
    }

    @Override
    public Page<ClientTeamEmailConfiguration> find(Long teamId, Pageable page) {
        if (page == null) {
            page = new PageRequest(0, 10);
        }

        return clientTeamEmailConfigurationRepository.findByTeamId(teamId, page);
    }

    @Override
    public ClientTeamEmailConfiguration save(
    ClientTeamEmailConfiguration clientTeamEmailConfiguration) {
        return clientTeamEmailConfigurationRepository.save(clientTeamEmailConfiguration);
    }

    @Override
    public ClientTeamEmailConfiguration reload(Long id) {
        return id != null ? clientTeamEmailConfigurationRepository.findOne(id) : null;
    }

}
