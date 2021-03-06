package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.persistence.ClientTeamSelfRegConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.LanguagePersistence;
import com.emmisolutions.emmimanager.persistence.PatientSelfRegConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.service.PatientSelfRegConfigurationService;
import com.emmisolutions.emmimanager.service.TeamService;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Collection;

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
    LanguagePersistence languagePersistence;

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
        setDefaults(patientSelfRegConfig);
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
        setDefaults(patientSelfRegConfig);
        return patientSelfRegConfigurationPersistence.save(patientSelfRegConfig);
    }

    private void setDefaults(PatientSelfRegConfig patientSelfRegConfig) {
        if (!patientSelfRegConfig.isExposePhone()) {
            patientSelfRegConfig.setRequirePhone(false);
        }
        if (!patientSelfRegConfig.isExposeEmail()) {
            patientSelfRegConfig.setRequireEmail(false);
        }
        if (!patientSelfRegConfig.isExposeId()) {
            patientSelfRegConfig.setRequireId(false);
        }
        if (!patientSelfRegConfig.isExposeName()) {
            patientSelfRegConfig.setRequireName(false);
        }
        if (!patientSelfRegConfig.isExposeDateOfBirth()) {
            patientSelfRegConfig.setRequireDateOfBirth(false);
        }
    }

    @Override
    public PatientSelfRegConfig findByTeam(Team team) {
        return patientSelfRegConfigurationPersistence.findByTeamId(team.getId());
    }

    @Override
    public PatientSelfRegConfig reload(PatientSelfRegConfig patientSelfRegConfig) {
        return patientSelfRegConfigurationPersistence.reload(patientSelfRegConfig.getId());
    }

    @Override()
    public Page<Language> findAllAvailableLanguages(Pageable page) {
        Pageable pageToFetch;
        if (page == null) {
            pageToFetch = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        } else {
            pageToFetch = page;
        }
        return languagePersistence.list(pageToFetch);
    }

    @Override
    public Collection<PatientIdLabelType> getAllPatientIdLabelTypes() {
        return patientSelfRegConfigurationPersistence.getAllPatientIdLabelTypes();
    }

    @Override
    public Page<Strings> findByString(String key, Pageable page) {
        Pageable pageToFetch;
        if (page == null) {
            pageToFetch = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        } else {
            pageToFetch = page;
        }
        return patientSelfRegConfigurationPersistence.findByString(key, pageToFetch);
    }
}
