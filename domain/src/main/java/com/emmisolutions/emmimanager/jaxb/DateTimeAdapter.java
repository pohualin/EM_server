package com.emmisolutions.emmimanager.jaxb;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * JAXB adapter to convert DateTime to/from String
 */
public class DateTimeAdapter
        extends XmlAdapter<String, DateTime> {

    /**
     * From String to DateTime
     *
     * @param v to convert
     * @return DateTime
     * @throws Exception
     */
    public DateTime unmarshal(String v) throws Exception {
        v = StringUtils.trimToNull(v);
        if (v == null) {
            return null;
        }
        return new DateTime(v);
    }

    /**
     * From DateTime to String
     *
     * @param v to convert
     * @return String
     * @throws Exception
     */
    public String marshal(DateTime v) throws Exception {
        if (v == null) {
            return null;
        }
        return v.toString();
    }

}
