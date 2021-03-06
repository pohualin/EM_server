package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.ClientTeamSelfRegConfiguration;
import com.emmisolutions.emmimanager.persistence.ClientTeamSelfRegConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ClientTeamSelfRegConfigurationRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Persistence implementation for ClientTeamSelfRegConfiguration entity.
 */
@Repository
public class ClientTeamSelfRegConfigurationPersistenceImpl implements
        ClientTeamSelfRegConfigurationPersistence {

    @Resource
    ClientTeamSelfRegConfigurationRepository clientTeamSelfRegConfigurationRepository;

    @Override
    public ClientTeamSelfRegConfiguration find(Long teamId) {
        return clientTeamSelfRegConfigurationRepository.findByTeamId(teamId);
    }


    @Override
    public ClientTeamSelfRegConfiguration save(
            ClientTeamSelfRegConfiguration clientTeamEmailConfiguration) {
        return clientTeamSelfRegConfigurationRepository.save(clientTeamEmailConfiguration);
    }

    @Override
    public ClientTeamSelfRegConfiguration reload(Long id) {
        if (id == null) {
            return null;
        }
        return clientTeamSelfRegConfigurationRepository.findOne(id);
    }

    @Override
    public ClientTeamSelfRegConfiguration findByCode(String code) {
        ClientTeamSelfRegConfiguration ret = null;
        if (StringUtils.isNotBlank(code)) {
            ret = clientTeamSelfRegConfigurationRepository.findByCodeIgnoreCase(code);
        }
        return ret;
    }
}
