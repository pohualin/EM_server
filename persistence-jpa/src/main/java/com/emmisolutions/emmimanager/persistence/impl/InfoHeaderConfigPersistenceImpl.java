package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.InfoHeaderConfig;
import com.emmisolutions.emmimanager.model.InfoHeaderConfigSearchFilter;
import com.emmisolutions.emmimanager.persistence.InfoHeaderConfigPersistence;
import com.emmisolutions.emmimanager.persistence.impl.specification.InfoHeaderConfigSpecifications;
import com.emmisolutions.emmimanager.persistence.repo.InfoHeaderConfigRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * Persistence implementation for ClientTeamSelfRegConfiguration entity.
 */
@Repository
public class InfoHeaderConfigPersistenceImpl implements
        InfoHeaderConfigPersistence {

    @Resource
    InfoHeaderConfigRepository infoHeaderConfigRepository;

    @Resource
    InfoHeaderConfigSpecifications infoHeaderConfigSpecifications;

    @Override
    public InfoHeaderConfig save(
            InfoHeaderConfig infoHeaderConfig) {
        return infoHeaderConfigRepository.save(infoHeaderConfig);
    }

    @Override
    public InfoHeaderConfig reload(InfoHeaderConfig infoHeaderConfig) {
        if (infoHeaderConfig == null || infoHeaderConfig.getId() == null) {
            return null;
        }
        return infoHeaderConfigRepository.findOne(infoHeaderConfig.getId());
    }

    @Override
    public Page<InfoHeaderConfig> list(Pageable page, InfoHeaderConfigSearchFilter searchFilter) {
        if (page == null) {
            // default pagination request if none
            page = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        }
        return infoHeaderConfigRepository.findAll(where(infoHeaderConfigSpecifications.byPatientSelfRegConfig(searchFilter)), page);
    }
}
