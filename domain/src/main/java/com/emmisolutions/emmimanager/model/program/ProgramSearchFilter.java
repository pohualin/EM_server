package com.emmisolutions.emmimanager.model.program;

import org.apache.commons.lang3.StringUtils;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgramSearchFilter;

import java.util.HashSet;
import java.util.Set;

/**
 * Allows for narrowing and searching the program list
 */
public class ProgramSearchFilter {

    private Set<String> terms = new HashSet<>();

    private Set<Specialty> specialties = new HashSet<>();
    
    private Client client;

    public Set<Specialty> getSpecialties() {
        return specialties;
    }

    public ProgramSearchFilter addTerm(String term) {
        if (StringUtils.isNotBlank(term)) {
            terms.add(term.trim().toLowerCase());
        }
        return this;
    }

    public ProgramSearchFilter addSpecialty(Specialty specialty) {
        if (specialty != null && specialty.getId() != null) {
            specialties.add(specialty);
        }
        return this;
    }

    public Set<String> getTerms() {
        return terms;
    }
    
    public ProgramSearchFilter client(Client client) {
        this.client = client;
        return this;
    }

    /**
     * Fetches the client filter
     *
     * @return client set on the filter
     */
    public Client client() {
        return client;
    }

}
