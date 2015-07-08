package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.PatientIdLabelType;
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

        if (patientSelfRegConfig.getIdLabelType() != null
                && patientSelfRegConfig.getIdLabelType().equals(PatientIdLabelType.OTHER)
                && (patientSelfRegConfig.getPatientIdLabelEnglish() == null || patientSelfRegConfig.getPatientIdLabelSpanish() == null)) {
            throw new InvalidDataAccessApiUsageException("Label cannot be null for patient id label type OTHER");
        }
        patientSelfRegConfig.setId(null);
        patientSelfRegConfig.setVersion(null);
        patientSelfRegConfig.setTeam(teamPersistence.reload(patientSelfRegConfig.getTeam()));
        return patientSelfRegConfigurationPersistence.save(patientSelfRegConfig);
    }

    @Override
    @Transactional
    public PatientSelfRegConfig update(
            PatientSelfRegConfig patientSelfRegConfig) {
        if (patientSelfRegConfig == null || patientSelfRegConfig.getId() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "PatientSelfRegConfig can not be null.");
        }

        if (patientSelfRegConfig.getIdLabelType() != null
                && patientSelfRegConfig.getIdLabelType().equals(PatientIdLabelType.OTHER)
                && (patientSelfRegConfig.getPatientIdLabelEnglish() == null || patientSelfRegConfig.getPatientIdLabelSpanish() == null)) {
            throw new InvalidDataAccessApiUsageException("Label cannot be null for patient id label type OTHER");
        }
        patientSelfRegConfig.setTeam(teamPersistence.reload(patientSelfRegConfig.getTeam()));
        return patientSelfRegConfigurationPersistence.save(patientSelfRegConfig);
    }

    @Override
    @Transactional
    public PatientSelfRegConfig findByTeam(Team team){
        if (team == null || team.getId() == null) {
            throw new InvalidDataAccessApiUsageException("Team cannot be null" + "to find PatientSelfRegConfig");
        }
        return patientSelfRegConfigurationPersistence.findByTeamId(team.getId());
    }

}
