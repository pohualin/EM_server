package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Language Persistence class
 */
public interface LanguagePersistence {

    /**
     * returns a page of languages based on page size
     *
     * @param page
     * @return page of languages
     */
    Page<Language> list(Pageable page);

    /**
     * reloads a given language
     *
     * @param language
     * @return language
     */
    Language reload(Language language);
}
