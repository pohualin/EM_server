package com.emmisolutions.emmimanager.model;

import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Filter used for Patient searching.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PatientSearchFilter {
    @XmlElement(name = "name")
    @XmlElementWrapper(name = "names")
    private Set<String> names;

    private Client client;

    private final Pattern phonePattern = Pattern.compile("([2-9][0-9][0-9])([2-9][0-9][0-9])([0-9][0-9][0-9][0-9])");

    private final Pattern accessCodePattern = Pattern.compile("1[0-9]{10}|2[0-9]{10}");

    private String phone;

    private String email;

    private Set<String> accessCodes;

    private PatientSearchFilter(){}

    public static PatientSearchFilter with(){
        return new PatientSearchFilter();
    }

    public PatientSearchFilter names(String... names){
        if (names != null) {
            if (this.names == null) {
                this.names = new HashSet<>();
            }
            Collections.addAll(this.names, names);
        }
        return this;
    }

    public Set<String> getNames() {
        return names;
    }

    public Client getClient() {
        return client;
    }

    public PatientSearchFilter client(Client client) {
        this.client = client;
        return this;
    }

    public String getPhone() {
        String ret = null;
        if (phone != null) {
            // re-format as hyphen separated if there are 10 numbers
            Matcher phoneMatcher = phonePattern.matcher(phone);
            if (phoneMatcher.matches()) {
                ret = String.format("%s-%s-%s",
                        phoneMatcher.group(1), phoneMatcher.group(2), phoneMatcher.group(3));
            }
        }
        return ret;
    }

    public PatientSearchFilter phone(String phone) {
        if (phone != null) {
            // strip out non-numeric characters
            this.phone = StringUtils.removePattern(phone, "[^\\d]*");
        }
        return this;
    }

    public String getEmail() {
        return email;
    }

    public PatientSearchFilter email(String email) {
        if (StringUtils.isNotBlank(email)) {
            this.email = email;
        }
        return this;
    }

    public Set<String> getAccessCodes() {
        return accessCodes;
    }

    public PatientSearchFilter accessCodes(String... accessCodes){
        if (accessCodes != null) {
            for (String accessCode : accessCodes) {
                if (accessCode != null && accessCodePattern.matcher(accessCode).matches()) {
                    if (this.accessCodes == null) {
                        this.accessCodes = new HashSet<>();
                    }
                    this.accessCodes.add(accessCode);
                }
            }
        }
        return this;
    }

    @Override
    public String toString() {
        return "PatientSearchFilter{" +
                "client=" + client +
                ", names=" + names +
                ", phone='" + getPhone() + '\'' +
                '}';
    }

}
