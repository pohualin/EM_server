package com.emmisolutions.emmimanager.jaxb;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateTimeAdapter
        extends XmlAdapter<String, DateTime> {

    public DateTime unmarshal(String v) throws Exception {
        v = StringUtils.trimToNull(v);
        if (v == null) {
            return null;
        }
        return new DateTime(v);
    }

    public String marshal(DateTime v) throws Exception {
        if (v == null) {
            return null;
        }
        return v.toString();
    }

}
