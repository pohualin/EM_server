package com.emmisolutions.emmimanager.jaxb;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * JAXB adapter to convert to/from LocalTime and String
 */
public class LocalTimeAdapter
        extends XmlAdapter<String, LocalTime> {

    /**
     * From String to LocalTime
     *
     * @param v to convert
     * @return DateTime
     * @throws Exception
     */
    public LocalTime unmarshal(String v) throws Exception {
        v = StringUtils.trimToNull(v);
        if (v == null) {
            return null;
        }
        return new LocalTime(v);
    }

    /**
     * From LocalTime to String
     *
     * @param v to convert
     * @return String
     * @throws Exception
     */
    public String marshal(LocalTime v) throws Exception {
        if (v == null) {
            return null;
        }
        return v.toString();
    }

}
