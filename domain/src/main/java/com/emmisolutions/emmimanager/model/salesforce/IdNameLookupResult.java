package com.emmisolutions.emmimanager.model.salesforce;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * A generic search result
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class IdNameLookupResult {

    private String id;
    private String name;

    public IdNameLookupResult(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
