package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.Strings;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Locale;

/**
 * Responsible for persistence of externalized Strings.
 */
public interface StringsPersistence {

    /**
     * Fetches all strings for a locale.
     *
     * @param locale to load them for
     * @return List of Strings
     */
    @Cacheable("fetchAllStringsByLocale")
    List<Strings> fetchAllStrings(Locale locale);
}
