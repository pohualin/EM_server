package com.emmisolutions.emmimanager.web.rest.client.model.configuration;

import com.emmisolutions.emmimanager.model.configuration.EmailRestrictConfiguration;
import com.emmisolutions.emmimanager.web.rest.client.model.PagedResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;

import javax.xml.bind.annotation.XmlRootElement;

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
