package com.emmisolutions.emmimanager.web.rest.model.client;

import javax.xml.bind.annotation.XmlRootElement;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.web.rest.model.BaseResource;

/**
 * A HATEOAS wrapper for a Client entity.
 */
@XmlRootElement(name = "client")
public class ClientResource extends BaseResource<Client> {
    
}
