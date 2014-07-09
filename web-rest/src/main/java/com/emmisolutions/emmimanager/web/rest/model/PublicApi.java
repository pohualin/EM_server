package com.emmisolutions.emmimanager.web.rest.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * The public API for this server
 */
@XmlRootElement(name = "api")
@XmlAccessorType(XmlAccessType.FIELD)
public class PublicApi {

    @XmlElement(name = "link")
    private List<Link> links;

    public PublicApi() {

    }

}
