package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.InfoHeaderConfig;
import com.emmisolutions.emmimanager.model.InfoHeaderConfigSearchFilter;
import com.emmisolutions.emmimanager.persistence.InfoHeaderConfigPersistence;
import com.emmisolutions.emmimanager.persistence.PatientSelfRegConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.service.InfoHeaderConfigService;
import com.emmisolutions.emmimanager.service.TeamService;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

/**
 * Service layer for InfoHeaderConfig
 */
@Service
public class InfoHeaderConfigServiceImpl implements
        InfoHeaderConfigService {

    @Resource
    TeamService teamService;

    @Resource
    TeamPersistence teamPersistence;

    @Resource
    PatientSelfRegConfigurationPersistence patientSelfRegConfigurationPersistence;

    @Resource
    InfoHeaderConfigPersistence infoHeaderConfigPersistence;

    @Override
    @Transactional
    public InfoHeaderConfig create(
            InfoHeaderConfig infoHeaderConfig) {
        if (infoHeaderConfig == null) {
            throw new InvalidDataAccessApiUsageException(
                    "infoHeaderConfig can not be null.");
        }

        infoHeaderConfig.setId(null);
        infoHeaderConfig.setVersion(null);
        infoHeaderConfig.setPatientSelfRegConfig(patientSelfRegConfigurationPersistence.reload(infoHeaderConfig.getPatientSelfRegConfig().getId()));
        return infoHeaderConfigPersistence.save(infoHeaderConfig);
    }

    @Override
    @Transactional
    public InfoHeaderConfig update(
            InfoHeaderConfig infoHeaderConfig) {
        if (infoHeaderConfig == null || infoHeaderConfig.getId() == null || infoHeaderConfig.getVersion() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "PatientSelfRegConfig can not be null.");
        }
        infoHeaderConfig.setPatientSelfRegConfig(patientSelfRegConfigurationPersistence.reload(infoHeaderConfig.getPatientSelfRegConfig().getId()));
        return infoHeaderConfigPersistence.save(infoHeaderConfig);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Page<InfoHeaderConfig> list(Pageable pageable, InfoHeaderConfigSearchFilter searchFilter) {
        return infoHeaderConfigPersistence.list(pageable, searchFilter);
    }

    @Override
    public InfoHeaderConfig reload(InfoHeaderConfig infoHeaderConfig) {
        return infoHeaderConfigPersistence.reload(infoHeaderConfig);
    }
}
