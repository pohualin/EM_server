package com.emmisolutions.emmimanager.web.rest.admin.model.configuration.contentSubscription;

import com.emmisolutions.emmimanager.model.configuration.ClientProgramContentInclusion;
import com.emmisolutions.emmimanager.web.rest.admin.model.BaseResource;

import javax.xml.bind.annotation.*;

/**
 * HATEOAS wrapper for ClientProgramContentInclusion
 */
@XmlRootElement(name = "client-program-content-inclusion")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClientProgramContentInclusionResource extends BaseResource<ClientProgramContentInclusion> {
}
