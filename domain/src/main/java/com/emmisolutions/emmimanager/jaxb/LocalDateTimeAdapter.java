package com.emmisolutions.emmimanager.jaxb;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDateTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LocalDateTimeAdapter
        extends XmlAdapter<String, LocalDateTime> {

    public LocalDateTime unmarshal(String v) throws Exception {
        v = StringUtils.trimToNull(v);
        if (v == null) {
            return null;
        }
        return new LocalDateTime(v);
    }

    public String marshal(LocalDateTime v) throws Exception {
        if (v == null) {
            return null;
        }
        return v.toString();
    }

}
