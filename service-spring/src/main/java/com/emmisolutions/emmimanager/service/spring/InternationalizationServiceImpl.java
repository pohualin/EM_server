package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Strings;
import com.emmisolutions.emmimanager.persistence.StringsPersistence;
import com.emmisolutions.emmimanager.service.InternationalizationService;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Locale;

/**
 * Implementation of i18n Services
 */
@Service
public class InternationalizationServiceImpl implements InternationalizationService {

    @Resource
    StringsPersistence stringsPersistence;

    @Override
    public JSONObject getAllStringsForLocale(Locale locale) {
        if (locale == null || StringUtils.isBlank(locale.toLanguageTag())) {
            locale = Locale.ENGLISH;
        }
        return expand(stringsPersistence.fetchAllStrings(locale));
    }

    private JSONObject expand(List<Strings> stringses) {
        JSONObject jsonObject = new JSONObject();
        for (Strings strings : stringses) {
            expand(jsonObject, strings.getKey(), strings.getValue());
        }
        return jsonObject;
    }

    @SuppressWarnings("unchecked")
    private void expand(JSONObject jsonObject, String key, String value) {
        int dotIndex = key.indexOf(".");
        if (dotIndex != -1) {
            String parentKey = key.substring(0, dotIndex);
            String remainingKey = key.substring(dotIndex + 1, key.length());
            JSONObject parentObject = (JSONObject) jsonObject.get(parentKey);
            if (parentObject == null) {
                parentObject = new JSONObject();
                jsonObject.put(parentKey, parentObject);
            }
            expand(parentObject, remainingKey, value);
        } else {
            // at the value
            jsonObject.put(key, value);
        }
    }
}
