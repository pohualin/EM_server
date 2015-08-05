package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Language;
import com.emmisolutions.emmimanager.model.Strings;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.StringsPersistence;
import com.emmisolutions.emmimanager.persistence.repo.LanguageRepository;
import com.emmisolutions.emmimanager.persistence.repo.StringsRepository;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Test for Strings persistence
 */
public class StringsPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    StringsRepository stringsRepository;

    @Resource
    StringsPersistence stringsPersistence;

    @Resource
    LanguageRepository languageRepository;

    /**
     * This saves two values with the same key to the database
     * and ensures that only the request language is returned
     */
    @Test
    public void getStrings(){
        Language english = languageRepository.findOne(1l);
        Language spanish = languageRepository.findByLanguageTag("es");

        String enUtfValue = "ℬusiness time";
        Strings en = new Strings();
        en.setLanguage(english);
        en.setKey("a.value.to.fetch");
        en.setValue(enUtfValue);
        en =stringsRepository.save(en);

        Strings es = new Strings();
        es.setLanguage(spanish);
        es.setKey("a.value.to.fetch");
        es.setValue("bueños ñoches"); //utf-8 to make sure
        es = stringsRepository.save(es);

        List<Strings> strings = stringsPersistence.fetchAllStrings(Locale.ENGLISH);
        assertThat("only en is present", strings, hasItem(en));
        for (Strings string : strings) {
            if (string.getId().equals(en.getId())){
                assertThat("utf is retained", string.getValue(), is(enUtfValue));
            }
        }
        assertThat("es is not present", strings, not(hasItem(es)));

    }
}
