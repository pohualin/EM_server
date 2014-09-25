package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.InternationalizationService;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Locale;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test that the i18n service works
 */
public class InternationalizationServiceTest extends BaseIntegrationTest {

    @Resource
    InternationalizationService i18nService;

    /**
     * Make sure we get some JSON back for ENGLISH
     */
    @Test
    public void fetchJson() {
        Map json = i18nService.getAllStringsForLocale(Locale.ENGLISH);
        assertThat("isn't empty", json.size() > 10, is(true));
    }

    /**
     * We shouldn't have anything for CANADA
     */
    @Test
    public void fetchBadLocale() {
        Map json = i18nService.getAllStringsForLocale(Locale.CANADA);
        assertThat("is empty", json.size() < 10, is(true));
    }

}
