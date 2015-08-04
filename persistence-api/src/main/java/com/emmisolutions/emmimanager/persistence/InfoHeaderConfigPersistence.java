package com.emmisolutions.emmimanager.persistence;


import com.emmisolutions.emmimanager.model.InfoHeaderConfig;
import com.emmisolutions.emmimanager.model.InfoHeaderConfigSearchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Persistence API for Client Team Self Registration Configuration.
 */
public interface InfoHeaderConfigPersistence {

    InfoHeaderConfig save(InfoHeaderConfig infoHeaderConfig);

    Page<InfoHeaderConfig> list(Pageable pageable, InfoHeaderConfigSearchFilter searchFilter);

    InfoHeaderConfig reload(InfoHeaderConfig infoHeaderConfig);

}
