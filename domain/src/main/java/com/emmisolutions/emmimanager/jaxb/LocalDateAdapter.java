package com.emmisolutions.emmimanager.jaxb;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LocalDateAdapter
        extends XmlAdapter<String, LocalDate> {

    public LocalDate unmarshal(String v) throws Exception {
        v = StringUtils.trimToNull(v);
        if (v == null) {
            return null;
        }
        return new LocalDate(v);
    }

    public String marshal(LocalDate v) throws Exception {
        if (v == null) {
            return null;
        }
        return v.toString();
    }

}
