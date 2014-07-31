package com.emmisolutions.emmimanager.jaxb;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDateTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * JAXB adapter to convert to/from LocalDateTime and String
 */
public class LocalDateTimeAdapter
        extends XmlAdapter<String, LocalDateTime> {

    /**
     * From String to LocalDateTime
     *
     * @param v to convert
     * @return DateTime
     * @throws Exception
     */
    public LocalDateTime unmarshal(String v) throws Exception {
        v = StringUtils.trimToNull(v);
        if (v == null) {
            return null;
        }
        return new LocalDateTime(v);
    }

    /**
     * From LocalDateTime to String
     *
     * @param v to convert
     * @return String
     * @throws Exception
     */
    public String marshal(LocalDateTime v) throws Exception {
        if (v == null) {
            return null;
        }
        return v.toString();
    }

}
