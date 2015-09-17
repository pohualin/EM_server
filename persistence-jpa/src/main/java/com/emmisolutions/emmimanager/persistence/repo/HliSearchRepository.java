package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.program.ProgramSearchFilter;
import com.emmisolutions.emmimanager.model.program.hli.HliSearchRequest;

public interface HliSearchRepository {

    /**
     * Return the search request for a search filter
     *
     * @param filter used to narrow the query
     * @return a cached search request
     */
    HliSearchRequest find(ProgramSearchFilter filter);

    /**
     * Cleans the cache of saved HliSearchRequests
     */
    int cacheClean();
}
