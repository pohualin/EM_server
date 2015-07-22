package com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.patientSelfReg;

import com.emmisolutions.emmimanager.model.Language;
import com.emmisolutions.emmimanager.web.rest.client.model.PagedResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.*;

/**
 * A page of language resources
 */
public class LanguageResourcePage extends PagedResource<LanguageResource> {

    /**
     * Creates a page of LanguageResource objects for serialization to
     * the front
     *
     * @param languageResources
     * @param languages
     */
    public LanguageResourcePage(PagedResources<LanguageResource> languageResources,
                                Page<Language> languages) {
        pageDefaults(languageResources, languages);
    }

    /**
     *
     * @param toAugment
     * @return
     */
    public static Link getAllLanguagesLink(Link toAugment) {
        UriTemplate uriTemplate = new UriTemplate(toAugment.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM)));
        return new Link(uriTemplate, toAugment.getRel());
    }
}
