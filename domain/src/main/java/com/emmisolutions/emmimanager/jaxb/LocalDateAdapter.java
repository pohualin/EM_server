package com.emmisolutions.emmimanager.jaxb;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * JAXB adapter to convert to/from LocalDate and String
 */
public class LocalDateAdapter
        extends XmlAdapter<String, LocalDate> {

    /**
     * From String to LocalDate
     *
     * @param v to convert
     * @return DateTime
     * @throws Exception
     */
    public LocalDate unmarshal(String v) throws Exception {
        v = StringUtils.trimToNull(v);
        if (v == null) {
            return null;
        }
        return new LocalDate(v);
    }

    /**
     * From LocalDate to String
     *
     * @param v to convert
     * @return String
     * @throws Exception
     */
    public String marshal(LocalDate v) throws Exception {
        if (v == null) {
            return null;
        }
        return v.toString();
    }

}
