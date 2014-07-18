/**
 * This is the root domain/persistence model for the application. The {@link javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters}
 * registered below are enabled to allow special JAXB conversions. However please note that these
 * conversions will only work for this root level package. If you create a sub-package, you will need to
 * re-register the adapters. This is how JAXB works, don't blame me ;-).
 */
@XmlJavaTypeAdapters({
        @XmlJavaTypeAdapter(type=DateTime.class,
                value=DateTimeAdapter.class),
        @XmlJavaTypeAdapter(type=LocalDate.class,
                value=LocalDateAdapter.class),
        @XmlJavaTypeAdapter(type=LocalTime.class,
                value=LocalTimeAdapter.class),
        @XmlJavaTypeAdapter(type=LocalDateTime.class,
                value=LocalDateTimeAdapter.class)
})
package com.emmisolutions.emmimanager.model;

import com.emmisolutions.emmimanager.jaxb.DateTimeAdapter;
import com.emmisolutions.emmimanager.jaxb.LocalDateAdapter;
import com.emmisolutions.emmimanager.jaxb.LocalDateTimeAdapter;
import com.emmisolutions.emmimanager.jaxb.LocalTimeAdapter;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;