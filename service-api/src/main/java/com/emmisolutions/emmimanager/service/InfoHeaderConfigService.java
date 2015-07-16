package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.InfoHeaderConfig;
import com.emmisolutions.emmimanager.model.InfoHeaderConfigSearchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * The InfoHeaderConfig Service
 */
public interface InfoHeaderConfigService {

    InfoHeaderConfig create(
            InfoHeaderConfig infoHeaderConfig);

    InfoHeaderConfig update(
            InfoHeaderConfig infoHeaderConfig);

    Page<InfoHeaderConfig> list(Pageable page, InfoHeaderConfigSearchFilter infoHeaderConfigSearchFilter);

    InfoHeaderConfig reload(InfoHeaderConfig infoHeaderConfig);

}
