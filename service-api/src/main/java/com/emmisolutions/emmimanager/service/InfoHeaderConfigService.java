package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.InfoHeaderConfig;
import com.emmisolutions.emmimanager.model.InfoHeaderConfigSearchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The InfoHeaderConfig Service
 */
public interface InfoHeaderConfigService {
    /**
     * Creates a new info header config
     *
     * @param infoHeaderConfig to save
     * @return the saved infoHeaderConfig
     */
    InfoHeaderConfig create(
            InfoHeaderConfig infoHeaderConfig);

    /**
     * Updates a given info Header Config
     *
     * @param infoHeaderConfig
     * @return the updated infoHeaderConfig
     */
    InfoHeaderConfig update(
            InfoHeaderConfig infoHeaderConfig);

    /**
     * Gets a list of all info header configs based on search filter
     *
     * @param page
     * @param infoHeaderConfigSearchFilter
     * @return page of info header configs
     */
    Page<InfoHeaderConfig> list(Pageable page, InfoHeaderConfigSearchFilter infoHeaderConfigSearchFilter);

    /**
     * Reloads a given info header config
     *
     * @param infoHeaderConfig
     * @return
     */
    InfoHeaderConfig reload(InfoHeaderConfig infoHeaderConfig);

}
