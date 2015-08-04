package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Language Persistence class
 */
public interface LanguagePersistence {

    Page<Language> list(Pageable page);

    Language reload(Language language);
}
