package com.emmisolutions.emmimanager.model;

import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Filter used for Patient searching.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PatientSearchFilter {
    @XmlElement(name = "name")
    @XmlElementWrapper(name = "names")
    private Set<String> names;

    private Client client;

    /**
     * Creates a search filter using status and names
     *
     * @param names  the names to filter by
     */
    public PatientSearchFilter(Client client, String... names) {
        this.setClient(client);
        if (names != null) {
            this.names = new HashSet<>();
            Collections.addAll(this.names, names);
        }
    }


    /**
     * Creates a search filter using status and names
     *
     */
    public PatientSearchFilter(Client client) {
        this.setClient(client);
    }

    public Set<String> getNames() {
        return names;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "PatientSearchFilter{" +
                "names=" + names +
                ", client=" + client +
                '}';
    }
}
