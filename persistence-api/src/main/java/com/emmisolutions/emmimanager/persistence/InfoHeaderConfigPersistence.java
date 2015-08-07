package com.emmisolutions.emmimanager.persistence;


import com.emmisolutions.emmimanager.model.InfoHeaderConfig;
import com.emmisolutions.emmimanager.model.InfoHeaderConfigSearchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Persistence API for Info Header Configuration
 */
public interface InfoHeaderConfigPersistence {

    /**
     * saved a given info header config
     *
     * @param infoHeaderConfig
     * @return infoHeaderConfig
     */
    InfoHeaderConfig save(InfoHeaderConfig infoHeaderConfig);

    /**
     * lists a page of info header configs based on page and search filter criteria
     *
     * @param pageable
     * @param searchFilter
     * @return a page of info header configs
     */
    Page<InfoHeaderConfig> list(Pageable pageable, InfoHeaderConfigSearchFilter searchFilter);

    /**
     * reloads a given info header config
     *
     * @param infoHeaderConfig
     * @return
     */
    InfoHeaderConfig reload(InfoHeaderConfig infoHeaderConfig);

}
