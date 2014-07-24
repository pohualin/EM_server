package com.emmisolutions.emmimanager.web.rest.model.client;

import com.emmisolutions.emmimanager.model.ClientRegion;
import com.emmisolutions.emmimanager.model.ClientTier;
import com.emmisolutions.emmimanager.model.ClientType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * Reference data for client editing
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ReferenceData {

    @XmlElement(name = "clientType")
    @XmlElementWrapper(name = "clientTypes")
    private ClientType[] clientTypes = ClientType.values();

    @XmlElement(name = "clientRegion")
    @XmlElementWrapper(name = "clientRegions")
    private ClientRegion[] clientRegions = ClientRegion.values();

    @XmlElement(name = "clientTiers")
    @XmlElementWrapper(name = "clientTier")
    private ClientTier[] clientTiers = ClientTier.values();

}
