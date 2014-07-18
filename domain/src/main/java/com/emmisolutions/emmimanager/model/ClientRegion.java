package com.emmisolutions.emmimanager.model;

import javax.xml.bind.annotation.XmlEnum;

/**
 * Client Regions
 */
@XmlEnum(String.class)
public enum ClientRegion {
    ALTERNATIVE, SOUTHEAST, NORTHEAST, MIDWEST, WEST
}
