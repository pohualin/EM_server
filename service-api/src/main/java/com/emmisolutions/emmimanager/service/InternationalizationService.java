package com.emmisolutions.emmimanager.service;

import java.util.Locale;
import java.util.Map;

/**
 * Services surrounding i18n operations
 */
public interface InternationalizationService {

    /**
     * Creates a JSON object (Map) for all externalized
     * strings within a locale.
     *
     * @param locale to fetch
     * @return a json string
     */
    Map getAllStringsForLocale(Locale locale);
}
