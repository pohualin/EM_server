package com.emmisolutions.emmimanager.model;

import javax.xml.bind.annotation.XmlEnum;

/**
 * The Client Types
 */
@XmlEnum(String.class)
public enum ClientType {
    PROVIDER, PHYSICIAN_PRACTICE, PAYER, PHARMA, OTHER
}
