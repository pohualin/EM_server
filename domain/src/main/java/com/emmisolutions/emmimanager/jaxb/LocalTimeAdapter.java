package com.emmisolutions.emmimanager.jaxb;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LocalTimeAdapter
        extends XmlAdapter<String, LocalTime> {

    public LocalTime unmarshal(String v) throws Exception {
        v = StringUtils.trimToNull(v);
        if (v == null) {
            return null;
        }
        return new LocalTime(v);
    }

    public String marshal(LocalTime v) throws Exception {
        if (v == null) {
            return null;
        }
        return v.toString();
    }

}
