package com.emmisolutions.emmimanager.web.rest.admin.resource;

import com.emmisolutions.emmimanager.service.InternationalizationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Locale;
import java.util.Map;

import static org.springframework.http.MediaType.*;

/**
 * Internationalization Endpoint.
 */
@RestController
@RequestMapping(value = "/webapi")
public class InternationalizationResource {

    @Resource
    InternationalizationService i18nService;

    /**
     * Fetch all of the messages for a language
     *
     * @param languageId to be used to fetch externalized strings
     * @return a Javascript Object (Map)
     */
    @RequestMapping(method = RequestMethod.GET,
            value = "/messages",
            consumes = {APPLICATION_OCTET_STREAM_VALUE, APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE},
            produces = "application/json;charset=UTF-8")
    public Map createStringsForLanguage(@RequestParam(required = false, value = "lang", defaultValue = "en") String languageId) {
        return i18nService.getAllStringsForLocale(new Locale(languageId));
    }
}
