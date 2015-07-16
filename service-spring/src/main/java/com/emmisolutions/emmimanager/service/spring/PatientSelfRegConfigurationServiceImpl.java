package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.PatientSelfRegConfig;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.persistence.ClientTeamSelfRegConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.PatientSelfRegConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.service.PatientSelfRegConfigurationService;
import com.emmisolutions.emmimanager.service.TeamService;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

/**
 * Service layer for PatientSelfRegConfig
 */
@Service
public class PatientSelfRegConfigurationServiceImpl implements
        PatientSelfRegConfigurationService {

    @Resource
    TeamService teamService;

    @Resource
    TeamPersistence teamPersistence;

    @Resource
    PatientSelfRegConfigurationPersistence patientSelfRegConfigurationPersistence;


    @Resource
    ClientTeamSelfRegConfigurationPersistence clientTeamSelfRegConfigurationPersistence;

    @Override
    @Transactional
    public PatientSelfRegConfig create(
            PatientSelfRegConfig patientSelfRegConfig) {
        if (patientSelfRegConfig == null) {
            throw new InvalidDataAccessApiUsageException(
                    "PatientSelfRegConfig can not be null.");
        }

        patientSelfRegConfig.setId(null);
        patientSelfRegConfig.setVersion(null);
        patientSelfRegConfig.setExposeName(true);
        patientSelfRegConfig.setRequireDateOfBirth(true);
        patientSelfRegConfig.setTeam(teamPersistence.reload(patientSelfRegConfig.getTeam()));
        return patientSelfRegConfigurationPersistence.save(patientSelfRegConfig);
    }

    @Override
    @Transactional
    public PatientSelfRegConfig update(
            PatientSelfRegConfig patientSelfRegConfig) {
        if (patientSelfRegConfig == null || patientSelfRegConfig.getId() == null || patientSelfRegConfig.getVersion() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "PatientSelfRegConfig can not be null.");
        }
        patientSelfRegConfig.setTeam(teamPersistence.reload(patientSelfRegConfig.getTeam()));
        patientSelfRegConfig.setExposeName(true);
        patientSelfRegConfig.setRequireDateOfBirth(true);
        return patientSelfRegConfigurationPersistence.save(patientSelfRegConfig);
    }

    @Override
    public PatientSelfRegConfig findByTeam(Team team) {
        return patientSelfRegConfigurationPersistence.findByTeamId(team.getId());
    }

    @Override
    public PatientSelfRegConfig reload(PatientSelfRegConfig patientSelfRegConfig) {
        return patientSelfRegConfigurationPersistence.reload(patientSelfRegConfig.getId());
    }
}
