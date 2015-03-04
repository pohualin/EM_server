package com.emmisolutions.emmimanager.web.rest.admin.model.configuration;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;

import com.emmisolutions.emmimanager.model.configuration.EmailRestrictConfiguration;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;

/**
 * A HATEOAS wrapper for a page of EmailRestrictConfigurationResource objects.
 */
@XmlRootElement(name = "email-restrict-configuration-page")
public class EmailRestrictConfigurationPage extends
        PagedResource<EmailRestrictConfigurationResource> {

    public EmailRestrictConfigurationPage() {
    }

    /**
     * Wrapped constructor
     * 
     * @param emailRestrictConfigurationResources
     *            to be wrapped
     * @param emailRestrictConfigurationPage
     *            the raw response
     */
    public EmailRestrictConfigurationPage(
            PagedResources<EmailRestrictConfigurationResource> emailRestrictConfigurationResources,
            Page<EmailRestrictConfiguration> emailRestrictConfigurationPage) {
        pageDefaults(emailRestrictConfigurationResources,
                emailRestrictConfigurationPage);
    }

}
