package com.emmisolutions.emmimanager.service.configuration.thymeleaf;

import org.joda.time.LocalDate;
import uk.co.gcwilliams.jodatime.thymeleaf.processors.JodaTimeExpressionObject;

import java.util.Locale;

/**
 * Extension to add LocalDate processing for thymeleaf
 */
public class JodaTimeExpressionObjectExtension extends JodaTimeExpressionObject {
    /**
     * Default constructor
     *
     * @param locale The current locale
     */
    public JodaTimeExpressionObjectExtension(Locale locale) {
        super(locale);
    }

    /**
     * Formats the localDate with the specified format
     *
     * @param localDate The datetime
     * @return The formatted date
     */
    public String format(LocalDate localDate, String format) {
        return format(localDate.toDateTimeAtStartOfDay(), format);
    }
}
