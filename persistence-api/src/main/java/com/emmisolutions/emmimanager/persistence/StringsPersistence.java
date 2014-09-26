package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.Strings;

import java.util.List;
import java.util.Locale;

/**
 * Responsible for persistence of externalized Strings.
 */
public interface StringsPersistence {

    /**
     * Fetches all strings for a locale.
     *
     * @return List of Strings
     */
    List<Strings> fetchAllStrings(Locale locale);
}
