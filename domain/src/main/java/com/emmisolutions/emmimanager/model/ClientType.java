package com.emmisolutions.emmimanager.model;

import javax.xml.bind.annotation.XmlEnum;

/**
 * Created by matt on 7/18/14.
 */
@XmlEnum(String.class)
public enum ClientType {
    PROVIDER, PHYSICIAN_PRACTICE, PAYER, PHARMA, OTHER
}
