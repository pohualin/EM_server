package com.emmisolutions.emmimanager.model;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Filter used for Patient searching.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PatientSearchFilter {

    private static final Pattern phonePattern = Pattern.compile("([2-9][0-9][0-9])([2-9][0-9][0-9])([0-9][0-9][0-9][0-9])");

    private static final Pattern accessCodePattern = Pattern.compile("1[0-9]{10}|2[0-9]{10}");

    @XmlElement(name = "name")
    @XmlElementWrapper(name = "names")
    private Set<String> names;

    private Client client;

    @XmlElement(name = "team")
    @XmlElementWrapper(name = "teams")
    private Set<Team> teams;

    @XmlElement(name = "phone")
    @XmlElementWrapper(name = "phones")
    private Set<String> phones;

    @XmlElement(name = "email")
    @XmlElementWrapper(name = "emails")
    private Set<String> emails;

    @XmlElement(name = "accessCode")
    @XmlElementWrapper(name = "accessCodes")
    private Set<String> accessCodes;

    private boolean loadLastScheduledProgram;

    private PatientSearchFilter() {
    }

    /**
     * Creates a new PatientSearchFilter
     *
     * @return a new filter
     */
    public static PatientSearchFilter with() {
        return new PatientSearchFilter();
    }

    /**
     * Adds a Set of 'normalized names' to search for
     *
     * @param names to find
     * @return this PatientSearchFilter for chaining
     */
    public PatientSearchFilter names(String... names) {
        if (names != null) {
            if (this.names == null) {
                this.names = new HashSet<>();
            }
            Collections.addAll(this.names, names);
        }
        return this;
    }

    /**
     * Retrieves all name filters
     *
     * @return the Set of names or null
     */
    public Set<String> names() {
        return names;
    }

    /**
     * Gets the client filter
     *
     * @return the Client or null
     */
    public Client client() {
        return client;
    }

    /**
     * Sets the Client filter
     *
     * @param client to set
     * @return this PatientSearchFilter for chaining
     */
    public PatientSearchFilter client(Client client) {
        this.client = client;
        return this;
    }

    /**
     * Get the Set of teams
     *
     * @return the Set of teams or null
     */
    public Set<Team> teams() {
        return teams;
    }

    /**
     * Sets the Team on which a patient has a scheduled program
     *
     * @param teams the Set of teams
     * @return this PatientSearchFilter for chaining
     */
    public PatientSearchFilter teams(Team... teams) {
        if (teams != null) {
            if (this.teams == null) {
                this.teams = new HashSet<>();
            }
            Collections.addAll(this.teams, teams);
        }
        return this;
    }

    /**
     * Gets the set of valid phones to filter on
     *
     * @return the Set of phone numbers
     */
    public Set<String> phones() {
        return phones;
    }

    /**
     * Adds phone numbers to the filter. Phone numbers will
     * only be added if the string contains 10 numbers, whitespace
     * and non-numeric characters are stripped
     *
     * @param phones to add
     * @return this PatientSearchFilter for chaining
     */
    public PatientSearchFilter phones(String... phones) {
        if (phones != null) {
            for (String phone : phones) {
                // strip out non-numeric characters
                String stripped = StringUtils.removePattern(phone, "[^\\d]*");
                Matcher phoneMatcher = phonePattern.matcher(stripped);
                // re-format as hyphen separated if there are 10 numbers
                if (phoneMatcher.matches()) {
                    if (this.phones == null) {
                        this.phones = new HashSet<>();

                    }
                    this.phones.add(String.format("%s-%s-%s",
                            phoneMatcher.group(1), phoneMatcher.group(2), phoneMatcher.group(3)));
                }
            }
        }
        return this;
    }

    /**
     * List based interface for phones
     *
     * @param phones the List of phones
     * @return this PatientSearchFilter for chaining
     */
    public PatientSearchFilter phones(List<String> phones) {
        if (!CollectionUtils.isEmpty(phones)) {
            this.phones(phones.toArray(new String[phones.size()]));
        }
        return this;
    }

    /**
     * Get emails
     *
     * @return the emails
     */
    public Set<String> emails() {
        return emails;
    }

    /**
     * Adds all non-blank strings as email filters
     *
     * @param emails to add
     * @return this PatientSearchFilter for chaining
     */
    public PatientSearchFilter emails(String... emails) {
        if (emails != null) {
            for (String email : emails) {
                if (StringUtils.isNotBlank(email)) {
                    if (this.emails == null) {
                        this.emails = new HashSet<>();
                    }
                    this.emails.add(StringUtils.trim(email));
                }
            }

        }
        return this;
    }

    public PatientSearchFilter emails(List<String> emails) {
        if (!CollectionUtils.isEmpty(emails)) {
            this.emails(emails.toArray(new String[emails.size()]));
        }
        return this;
    }

    /**
     * Get access codes which have been set
     *
     * @return Set of access codes
     */
    public Set<String> accessCodes() {
        return accessCodes;
    }

    /**
     * Adds valid access code strings to the filter. Access
     * codes are in the form of 1XXXXXXXXXXX and 2XXXXXXXXXXX
     * where X is a number.
     *
     * @param accessCodes to add
     * @return this PatientSearchFilter for chaining
     */
    public PatientSearchFilter accessCodes(String... accessCodes) {
        if (accessCodes != null) {
            for (String accessCode : accessCodes) {
                String trimmed = StringUtils.trimToNull(accessCode);
                if (trimmed != null && accessCodePattern.matcher(trimmed).matches()) {
                    if (this.accessCodes == null) {
                        this.accessCodes = new HashSet<>();
                    }
                    this.accessCodes.add(trimmed);
                }
            }
        }
        return this;
    }

    /**
     * List based input for accessCodes
     * @param accessCodes to add
     * @return this PatientSearchFilter for chaining
     */
    public PatientSearchFilter accessCodes(List<String> accessCodes) {
        if (!CollectionUtils.isEmpty(accessCodes)) {
            this.accessCodes(accessCodes.toArray(new String[accessCodes.size()]));
        }
        return this;
    }

    /**
     * Whether or not to load the last scheduled program for the patient
     *
     * @return true to load
     */
    public boolean needToLoadLastScheduledProgram() {
        return loadLastScheduledProgram;
    }

    /**
     * Want to load the last scheduled program
     *
     * @return this PatientSearchFilter for chaining
     */
    public PatientSearchFilter lastScheduledProgramLoaded() {
        this.loadLastScheduledProgram = true;
        return this;
    }

    @Override
    public String toString() {
        return "PatientSearchFilter{" +
                ", names=" + names +
                ", client=" + client +
                ", teams=" + teams +
                ", phones=" + phones +
                ", emails=" + emails +
                ", accessCodes=" + accessCodes +
                '}';
    }

}
