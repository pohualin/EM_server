package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Strings;
import com.emmisolutions.emmimanager.persistence.StringsPersistence;
import com.emmisolutions.emmimanager.persistence.repo.StringsRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Locale;

/**
 * Spring Data/JPA implementation
 */
@Repository
public class StringsPersistenceImpl implements StringsPersistence {

    @Resource
    StringsRepository stringsRepository;

    @Override
    public List<Strings> fetchAllStrings(Locale locale) {
        return stringsRepository.findByLanguageLanguageTag(locale.toLanguageTag());
    }
}
