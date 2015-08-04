package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Language;
import com.emmisolutions.emmimanager.model.Strings;
import com.emmisolutions.emmimanager.persistence.StringsPersistence;
import com.emmisolutions.emmimanager.persistence.repo.LanguageRepository;
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

    @Resource
    LanguageRepository languageRepository;

    @Override
    public List<Strings> fetchAllStrings(Locale locale) {
        return stringsRepository.findByLanguageLanguageTag(locale.toLanguageTag());
    }

//    @Override
//    public List<Strings> fetchAllSpanishStringsForIdLabelTypes(Locale locale) {
//        return stringsRepository.findByLanguageLanguageTag(languageRepository.findByLanguageTag("es"));
//    }

    @Override
    public Strings findByLanguageAndString(Language language, String string){
        return stringsRepository.findByLanguageAndKey(language, string);
    }
}
