package com.emmisolutions.emmimanager.web.rest.admin.model.configuration;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;

import com.emmisolutions.emmimanager.model.configuration.IpRestrictConfiguration;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;

/**
 * A HATEOAS wrapper for a page of IpRestrictConfigurationResource objects.
 */
@XmlRootElement(name = "ip-restrict-configuration-page")
public class IpRestrictConfigurationPage extends
        PagedResource<IpRestrictConfigurationResource> {

    public IpRestrictConfigurationPage() {
    }

    /**
     * Wrapped constructor
     * 
     * @param ipRestrictConfigurationResources
     *            to be wrapped
     * @param ipRestrictConfigurationPage
     *            the raw response
     */
    public IpRestrictConfigurationPage(
            PagedResources<IpRestrictConfigurationResource> ipRestrictConfigurationResources,
            Page<IpRestrictConfiguration> ipRestrictConfigurationPage) {
        pageDefaults(ipRestrictConfigurationResources,
                ipRestrictConfigurationPage);
    }

}
