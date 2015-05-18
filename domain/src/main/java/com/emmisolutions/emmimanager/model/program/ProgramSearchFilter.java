package com.emmisolutions.emmimanager.model.program;

import java.util.HashSet;
import java.util.Set;

/**
 * Allows for narrowing and searching the program list
 */
public class ProgramSearchFilter {

   private Set<Specialty> specialties = new HashSet<>();

    public Set<Specialty> getSpecialties() {
        return specialties;
    }

    public ProgramSearchFilter addSpecialty(Specialty specialty){
        if (specialty != null && specialty.getId() != null) {
            specialties.add(specialty);
        }
        return this;
    }

}
